package kr.stam.homepage.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.stam.homepage.dao.DepthDao;
import kr.stam.homepage.dao.ProductDao;
import kr.stam.homepage.dao.ProductLogDao;
import kr.stam.homepage.dto.DepthDto;
import kr.stam.homepage.dto.ProductDto;
import kr.stam.homepage.dto.ProductLogDto;

@Controller
public class ProductController {
	@Autowired
	private ProductDao pd;

	@Autowired
	private DepthDao dDao;

	@Autowired
	private ProductLogDao pLDao;

	@RequestMapping("product")
	public String product(HttpServletRequest request, Model model, HttpSession session, String menuContents,
			String menuParents) {
		if (session.getAttribute("level") != null) {
			Integer ProductnextNum = pd.ProductnextNum();
			session.setAttribute("ProductnextNum", ProductnextNum + 1);

			ArrayList<DepthDto> Flist = dDao.Flist();
			model.addAttribute("Flist", Flist);

			ArrayList<ProductDto> list = pd.list(menuContents);
			session.setAttribute("menuContents", menuContents);
			session.setAttribute("menuParents", menuParents);
			model.addAttribute("list", list);

			return "product/product";
		} else {
			return "redirect:/main";
		}
	}

	@RequestMapping("/product_insert")
	public String depth_insert(HttpServletRequest request, Model model, HttpSession session) {
		String menuParents = (String) session.getAttribute("menuParents");
		ArrayList<DepthDto> Flist = dDao.Flist();
		model.addAttribute("Flist", Flist);
		if (session.getAttribute("level") != null) {

			return "product/product_insert";
		}

		else {

			return "redirect:/product";
		}
	}

	@RequestMapping(value = "/product_insert_ok", method = RequestMethod.POST)
	public String product_insert_ok(ProductDto pdto, ProductLogDto pLdto, HttpSession session,
			MultipartHttpServletRequest mRequest, String menuContents) throws Exception, IOException {

		if (session.getAttribute("ProductnextNum") != null) {
			int ProductnextNum = (int) session.getAttribute("ProductnextNum");
			System.out.println("인서터 세션 :" + ProductnextNum);
			pLdto.setProductCode(ProductnextNum + 1);
		} else {
			int ProductnextNum = 1;
			pLdto.setProductCode(ProductnextNum);
		}

//		String uploadFolder2 = "C:\\Users\\woonho\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images\\portfolio";
//		String uploadFolder = "C:\\Users\\woonho\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images\\logo";

		String uploadFolder = "C:\\Users\\stam\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images\\logo";
		String uploadFolder2 = "C:\\Users\\stam\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images\\product";
		MultipartFile multipartFileLogo = mRequest.getFile("pLogo");
		MultipartFile multipartFileProduct = mRequest.getFile("pImg");
		System.out.println("확인용 : " + multipartFileLogo + multipartFileProduct);
		if (!multipartFileLogo.isEmpty() && !multipartFileProduct.isEmpty()) { // 로고, 제품사진 모두 있을경우
			pdto.setCompanyLogo(multipartFileLogo.getOriginalFilename());
			File saveFileLogo = new File(uploadFolder, multipartFileLogo.getOriginalFilename());
			multipartFileLogo.transferTo(saveFileLogo);

			pdto.setProductImg(multipartFileProduct.getOriginalFilename());
			File saveFileProduct = new File(uploadFolder2, multipartFileProduct.getOriginalFilename());

			try {
				multipartFileProduct.transferTo(saveFileProduct);
			} catch (Exception e) {
				System.out.println("둘다  저장 실패");
			}
		} else if (!multipartFileLogo.isEmpty() && multipartFileProduct.isEmpty()) { // 로고 사진만 있을경우

			pdto.setCompanyLogo(multipartFileLogo.getOriginalFilename());
			File saveFileLogo = new File(uploadFolder, multipartFileLogo.getOriginalFilename());

			try {

				multipartFileLogo.transferTo(saveFileLogo);
				pdto.setCompanyLogoData(multipartFileLogo);
			} catch (Exception e) {
				System.out.println("로고 저장 실패");
			}

			pdto.setProductImg(" ");
		} else if (multipartFileLogo.isEmpty() && !multipartFileProduct.isEmpty()) { // 제품 사진만있을경우
			pdto.setCompanyLogo(multipartFileLogo.getOriginalFilename());
			File saveFileLogo = new File(uploadFolder, multipartFileLogo.getOriginalFilename());

			multipartFileLogo.transferTo(saveFileLogo);
			pdto.setCompanyLogo(" ");
		} else { // 둘다 없을경우
			pdto.setCompanyLogo(" ");
			pdto.setProductImg(" ");
		}

		pd.insert(pdto);

		String managerId = (String) session.getAttribute("mId");
		String managerName = (String) session.getAttribute("mName");

		pLdto.setManagerId(managerId);
		pLdto.setManagerName(managerName);

		pLdto.setPLogType("Insert");

		pLDao.insert(pLdto);
		return "redirect:/product?menuContents=" + menuContents;
	}

	@ResponseBody
	@RequestMapping(value = "/product_delete", method = RequestMethod.POST)
	public int product_delete(HttpSession session, ProductLogDto pLdto,
			@RequestParam(value = "chbox[]") List<String> chArr, ProductDto pDto) throws Exception {
		int result = 0;

		String managerId = (String) session.getAttribute("mId");
		String managerName = (String) session.getAttribute("mName");
		for (String i : chArr) {
			int productCode = Integer.parseInt(i);
			pDto.setProductCode(productCode);
			pd.delete(pDto);
			pLdto.setProductCode(productCode);
			pLdto.setManagerId(managerId);
			pLdto.setManagerName(managerName);
			pLdto.setPLogType("delete");
			pLDao.insert(pLdto);
		}

		result = 1;

		return result;
	}

	@RequestMapping("/product_content")
	public String content(HttpServletRequest request, ProductDto pDto, Model model, HttpSession session)
			throws Exception {
		if (session.getAttribute("level") != null) {
			int productCode = Integer.parseInt(request.getParameter("productCode"));
			String menuContents = (String) session.getAttribute("menuContents");
			Integer listCount = pd.listCount(menuContents);
			pDto.setListCount(listCount);
			ArrayList<DepthDto> Flist = dDao.Flist();
			model.addAttribute("Flist", Flist);
			pDto = pd.content(productCode);
			model.addAttribute("pDto", pDto);
			return "product/product_content";
		} else {
			return "redirect:/main";
		}
	}

	@RequestMapping("/product_update")
	public String product_update(HttpServletRequest request, Model model, HttpSession session, ProductDto pDto) {

		ArrayList<DepthDto> Flist = dDao.Flist();
		model.addAttribute("Flist", Flist);
		if (session.getAttribute("level") != null) {
			int productCode = Integer.parseInt(request.getParameter("productCode"));

			pDto = pd.content(productCode);
			session.setAttribute("productCode", productCode);
			model.addAttribute("pDto", pDto);
			return "product/product_update";
		}

		else {

			return "redirect:/main";
		}
	}

	@RequestMapping("product_update_ok")
	public String update_ok(HttpServletRequest request, ProductLogDto pLdto, ProductDto pDto, HttpSession session,
			MultipartHttpServletRequest mRequest) throws Exception, IOException {

		int productCode = (int) session.getAttribute("productCode");
		pDto.setProductCode(productCode);
		String menuContents = request.getParameter("menuContents");
//		String uploadFolder2 = "C:\\Users\\woonho\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images\\portfolio";
//		String uploadFolder = "C:\\Users\\woonho\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images\\logo";
		String uploadFolder = "C:\\Users\\stam\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images\\logo";
		String uploadFolder2 = "C:\\Users\\stam\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images\\product";
		MultipartFile multipartFileLogo = mRequest.getFile("pLogo");
		MultipartFile multipartFileProduct = mRequest.getFile("pImg");
//로고
		int logoCancleflag = Integer.parseInt(request.getParameter("logoCancleflag"));

		if (logoCancleflag == 0) { // 이전 이미지 그대로 사용
			String companyLogo = request.getParameter("companyLogo");
			System.out.println(companyLogo);
			pDto.setCompanyLogo(companyLogo);

		} else if (logoCancleflag == 1) { // 새로운 이미지 사용

			pDto.setCompanyLogo(multipartFileLogo.getOriginalFilename());
			File saveFileLogo = new File(uploadFolder, multipartFileLogo.getOriginalFilename());
			multipartFileLogo.transferTo(saveFileLogo);

		} else { // 이미지 사용안함

			pDto.setCompanyLogo(" ");
		}
// 제품
		int productCancleFlag = Integer.parseInt(request.getParameter("productCancleflag"));
		if (productCancleFlag == 0) { // 이전 이미지 그대로 사용
			String productImg = request.getParameter("productImg");
			pDto.setProductImg(productImg);

		} else if (productCancleFlag == 1) { // 새로운 이미지 사용

			pDto.setProductImg(multipartFileProduct.getOriginalFilename());
			File saveFileProduct = new File(uploadFolder2, multipartFileProduct.getOriginalFilename());
			multipartFileProduct.transferTo(saveFileProduct);

		} else { // 이미지 사용안함

			pDto.setProductImg(" ");
		}


		String managerId = (String) session.getAttribute("mId");
		String managerName = (String) session.getAttribute("mName");
		pd.update(pDto);
		pLdto.setProductCode(productCode);
		pLdto.setManagerId(managerId);
		pLdto.setManagerName(managerName);
		pLdto.setPLogType("update");
		pLDao.insert(pLdto);
		return "redirect:/product?menuContents=" + menuContents;
	}
}
