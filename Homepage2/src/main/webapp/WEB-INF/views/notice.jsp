<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>STAM</title>
<link rel="stylesheet" href="resource/plugins/swiper/swiper-bundle.min.css"/>
<link rel="stylesheet" href="resource/css/common.css">
<link rel="stylesheet" href="resource/css/style.css">
<link rel="stylesheet" href="resource/css/notice.css">
<script src="resource/js/jquery-3.2.1.min.js"></script>
<script src="resource/plugins/swiper/swiper-bundle.min.js"></script>
<script src="resource/js/common.js"></script>
</head>
<body>
<div class="wrapper" id="mainWrapper">
 <header class="header item">
    <h1 class="logo"><a href="main"><img src="resource/images/logo.png" alt="STAM"></a></h1>
    <button class="btn_nav mo">
      <span></span><span></span><span></span>
    </button>
    <button class="btn_back mo"></button>
    <nav class="navi_wrap">
      <ul class="navi">
        <li>
          <a href="intro" class="depth1">ABOUT</a>
          <ul class="depth2">
            <li><a href="intro">Intro + Vision & Business</a></li>
            <li><a href="about_history">History</a></li>
            <li><a href="organizationChart">Organization Chart</a></li>
            <li><a href="withClient">With CLIENT</a></li>
          </ul>
        </li>
        <li>
          <a href="portfolio_2021" class="depth1">IT portfolio</a>
          <ul class="depth2">
            <li><a href="portfolio_2021">2021</a></li>
            <li><a href="portfolio_2020">2020</a></li>
            <li><a href="portfolio_2019">2019</a></li>
            <li><a href="portfolio_2018">2018</a></li>
            <li><a href="portfolio_2017">2017</a></li>
            <li><a href="portfolio_2016">-2016</a></li>
          </ul>
        </li>
        <li>
          <a href="brand" class="depth1">Brand</a>
          <ul class="depth2">
            <li><a href="brand">Brand</a></li>
            <li><a href="brand_promotion">Promotion</a></li>
            <li><a href="brand_edit">Edit</a></li>
          </ul>
        </li>
        <li>
          <a href="solution_Adrive" class="depth1">SOLUTION</a>
          <ul class="depth2">
            <li><a href="solution_Adrive">A.Drive</a></li>
            <li><a href="solution_ST_framework">ST Framework</a></li>
            <li><a href="solution_system1">통합 SNS 관리 시스템</a></li>
            <li><a href="solution_system2">구간별 암호화 시스템</a></li>
          </ul>
        </li>
        <li>
        	 <a href="notice" class="depth1">Notice</a>
        </li>
      </ul>
    </nav>
  </header>

  <div class="container item">

    <div class="swiper-container">
      <div class="swiper-wrapper">

        <div class="swiper-slide">
          <div class="loca_div mo">
            <span class="loca">HOME</span>
            <span class="slogan">Strategy+Ambition</span>
          </div>
          <div class="notice-div">
          	<span class="notice-span">Notice</span>
       		<table class="notice-table">
       		<tr> <!-- 검색행 -->
		      <td colspan="4">
		        <form method="post" action="notice">
		                        선택 <select name="cla">
		             <option value="ntitle"> 제목 </option>
		             <option value="mwriter"> 작성자 </option>
		             <option value="ncontent"> 내용 </option>            
		            </select>
		                         검색어 <input type="text" name="sword">
		          <input type="submit" value="검색">
		        </form>
		      </td>
		     </tr>
       			<tr class="notice-tr top-tr">
       				<td class="notice-th td-num">번호</td>
       				<td class="notice-th td-mangager">작성자</td>
       				<td class="notice-th Td-Title">제목</td>
       				<td class="notice-th Td-Regdate">날짜</td>
       			</tr>
       			<c:forEach var="ndto" items="${nlist}">
       			<tr class="notice-tr">
       				<td class="notice-td">${ndto.nid }</td>
       				<td class="notice-td">${ndto.nwriter }</td>
       				<td class="notice-td"><a href="content?nid=${ndto.nid }">${ndto.ntitle }</a></td>
       				<td class="notice-td">${ndto.regdate }</td>
       			</tr>
       			</c:forEach>
<!-- 페이징 html -->
       			<tr>
			      <td colspan="4" align="center">
			       <!-- 이전 페이지 이동 -->
			         <!-- 10페이지 이전 이동 시작 -->
			          <c:if test="${pstart != 1}">
			           <a href="notice?page=${pstart-1}&sword=${sword}"> << </a>
			          </c:if>
			          <c:if test="${pstart == 1}">
			           <<
			          </c:if>
			         <!-- 10페이지 이전 이동 끝 -->
			         
			         <!-- 1페이지 이전 이동 (현재페이지에서 1페이지 이전) -->
			         <c:if test="${page > 1}">
			          <a href="notice?page=${page-1}&sword=${sword}"> < </a>  
			         </c:if>
			         <c:if test="${page == 1}"> 
			          <      
			         </c:if>
			         <!-- 1페이지 이전 이동 끝 -->
			       <!-- 이전 페이지 이동 끝 -->
			       <c:forEach begin="${pstart}" end="${pend}" var="i">
			        <c:if test="${page == i}">
			         <a href="notice?page=${i}&sword=${sword}" style="color:red;"> ${i} </a>
			        </c:if>
			        <c:if test="${page != i}">
			         <a href="notice?page=${i}&sword=${sword}"> ${i} </a>
			        </c:if>
			       </c:forEach>
			       
			       <!-- 다음 페이지 이동 -->
			        <!-- 다음 1페이지 이동 -->
			        <c:if test="${page != pagecnt }">
			         <a href="notice?page=${page+1}&sword=${sword}"> > </a>
			        </c:if>
			        <c:if test="${page == pagecnt}"> 
			         >
			        </c:if>
			        <!-- 다음 1페이지 이동 끝 -->
			        
			        <!-- 다음 10페이지 이동 시작 -->
			        <c:if test="${pend != pagecnt }">
			         <a href="notice?page=${pend+1}&sword=${sword}"> >> </a>
			        </c:if>
			        <c:if test="${pend == pagecnt}">
			          >>
			        </c:if>
			        <!-- 다음 10페이지 이동 끝 -->
			       <!-- 다음 페이지 이동 끝 -->
			      </td>
			    </tr>
<!-- 페이징 html 끝 -->
       			<tr>
	       			<td><a href="insert" class="notice_insert">추가</a></td>
	       		</tr>
       		</table>
       		
          </div>

      </div><!-- //swiper-wrapper -->
      <div class="swiper-pagination"></div>
    </div><!-- //swiper-container -->

  </div><!-- //container -->

</div>
</div>


</body>

</html>