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
    <title>Spring Boot Unknown Error PAGE</title>
    <!--/* 이 영역에 공통으로 사용할 css, js library를 선언한다. */-->
    <link th:href="@{/css/common.css}" rel="stylesheet" />
</th:block>

<body>
    <th:block layout:fragment="header" th:include="@{/fragments/header}"></th:block>
    <h1>This is Unknown Error Page!</h1> Unknown Server Error... <br>
    <th:block layout:fragment="footer" th:include="@{/fragments/footer}"></th:block>
</body>

</html>