<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<c:if test="${ menuList == null}">
		<h1>Hello World</h1>
	</c:if>
	
	<c:forEach items="${menuList}" var="item">
		<h5>${item}</h5>
	</c:forEach>
</body>
</html>