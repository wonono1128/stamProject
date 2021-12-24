<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{layout/default_layout}">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<th:block layout:fragment="head">
	<title>Spring Boot 405 Error PAGE</title>
	<!--/* 이 영역에 공통으로 사용할 css, js library를 선언한다. */-->
	<link th:href="@{/css/common.css}" rel="stylesheet" />
</th:block>
<body>
	<th:block layout:fragment="header" th:include="@{/fragments/header}"></th:block>
	<h1>This is 405 Error Page!</h1>
	<div>
		CODE: <span th:text="${code}"></span><br> MSG: <span
			th:text="${msg}"></span><br> TIMESTAMP: <span
			th:text="${timestamp}"></span><br>
	</div>
	<th:block layout:fragment="footer" th:include="@{/fragments/footer}"></th:block>
</body>

</html>