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
			String referer = (String) request.getHeader("REFERER");
			int contentsIndex = referer.lastIndexOf("=");
			String menuContents = referer.substring(contentsIndex+1);
			System.out.println("이전주소 : " + menuContents);
			session.setAttribute("referMenuContents", menuContents);
			return "product/product_insert";
		}

		else {

			return "redirect:/product";
		}
	}

	@RequestMapping(value = "/product_insert_ok", method = RequestMethod.POST)
	public String product_insert_ok(ProductDto pdto, ProductLogDto pLdto, HttpSession session, MultipartFile[] pLogo,
			MultipartFile[] pImg, MultipartFile[] yearLogo, String menuContents) {
	
		
		if (session.getAttribute("ProductnextNum") != null) {
			int ProductnextNum = (int) session.getAttribute("ProductnextNum");
			System.out.println("인서터 세션 :" + ProductnextNum);
			pLdto.setProductCode(ProductnextNum + 1);
		}else {
			int ProductnextNum = 1;
		}
		
		String uploadFolder = "C:\\Users\\stam\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images\\logo";
		String uploadFolder2 = "C:\\Users\\stam\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images\\product";
		String uploadFolder3 = "C:\\Users\\stam\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images";

//		String uploadFolder = "C:\\Users\\woonho\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images\\logo";
//		String uploadFolder2 = "C:\\Users\\woonho\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images\\product";
//		String uploadFolder3 = "C:\\Users\\woonho\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images";
		for (MultipartFile multipartFile : pLogo) {
			System.out.println("---------------------------로고 파일------------------------------------");
			System.out.println("Upload File Name : " + multipartFile.getOriginalFilename());
			System.out.println("Upload File Size : " + multipartFile.getSize());
			pdto.setCompanyLogo(multipartFile.getOriginalFilename());
			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());

			try {
				multipartFile.transferTo(saveFile);

			} catch (Exception e) {
				System.out.println("저장 실패");
			}
		}

		for (MultipartFile multipartFile : yearLogo) {
			System.out.println("---------------------------년도 로고 파일------------------------------------");
			System.out.println("Upload File Name : " + multipartFile.getOriginalFilename());
			System.out.println("Upload File Size : " + multipartFile.getSize());
			pdto.setProductLogo(multipartFile.getOriginalFilename());
			File saveFile = new File(uploadFolder3, multipartFile.getOriginalFilename());

			try {
				multipartFile.transferTo(saveFile);

			} catch (Exception e) {
				System.out.println("저장 실패");
			}
		}
		
		for (MultipartFile multipartFile : pImg) {
			System.out.println("---------------------------제품이미지 파일------------------------------------");
			System.out.println("Upload File Name : " + multipartFile.getOriginalFilename());
			System.out.println("Upload File Size : " + multipartFile.getSize());
			pdto.setProductImg(multipartFile.getOriginalFilename());
			File saveFile = new File(uploadFolder2, multipartFile.getOriginalFilename());

			try {
				multipartFile.transferTo(saveFile);
			} catch (Exception e) {
				System.out.println("저장 실패");
			}
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
//		String uploadFolder3 = "C:\\Users\\woonho\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images";
//		String uploadFolder2 = "C:\\Users\\woonho\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images\\product";
//		String uploadFolder = "C:\\Users\\woonho\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images\\logo";
		String uploadFolder = "C:\\Users\\stam\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images\\logo";
		String uploadFolder2 = "C:\\Users\\stam\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images\\product";
		String uploadFolder3 = "C:\\Users\\stam\\git\\StamProject\\Homepage\\src\\main\\resources\\static\\images";
		MultipartFile multipartFileLogo = mRequest.getFile("pLogo");
		MultipartFile multipartFileProduct = mRequest.getFile("pImg");
		MultipartFile multipartFileYear = mRequest.getFile("yearLogo");
//고객 로고
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
// 화면이미지
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
// 사업종류
		int yearCancleFlag = Integer.parseInt(request.getParameter("yearCancleflag"));
		if (yearCancleFlag == 0) { // 이전 이미지 그대로 사용
			String yearImg = request.getParameter("yearImg");
			pDto.setProductLogo(yearImg);

		} else if (yearCancleFlag == 1) { // 새로운 이미지 사용

			pDto.setProductLogo(multipartFileYear.getOriginalFilename());
			File saveFileYear = new File(uploadFolder3, multipartFileYear.getOriginalFilename());
			multipartFileYear.transferTo(saveFileYear);

		} else { // 이미지 사용안함

			pDto.setProductLogo(" ");
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
