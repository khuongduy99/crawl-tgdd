<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
  
  <style type="text/css">
  a {
  	text-decoration: none !important;
  }
  
  #button-back-top {
  display: inline-block;
  background-color: #FF9800;
  width: 50px;
  height: 50px;
  text-align: center;
  border-radius: 4px;
  position: fixed;
  bottom: 30px;
  right: 30px;
  transition: background-color .3s, 
    opacity .5s, visibility .5s;
  opacity: 0;
  visibility: hidden;
  z-index: 1000;
}
#button-back-top::after {
  content: "\f077";
  font-family: FontAwesome;
  font-weight: normal;
  font-style: normal;
  font-size: 2em;
  line-height: 50px;
  color: #fff;
}
#button-back-top:hover {
  cursor: pointer;
  background-color: #333;
}
#button-back-top:active {
  background-color: #555;
}
#button-back-top.show {
  opacity: 1;
  visibility: visible;
}
  </style>
</head>
<body style="padding: 86px;">
 <a id="button-back-top"></a>
<nav class="navbar navbar-expand-sm bg-dark navbar-dark fixed-top">
  <a class="navbar-brand" href="<c:url value='/'/>" style="width:100px"><img alt="" width="100%" src="https://rubee.com.vn/admin/webroot/upload/image/images/logo-the-gioi-di-dong-2.jpg"></a>
  <ul class="navbar-nav">
  <c:forEach items="${menuList}" var="item">
    <li class="nav-item">
    
      <a class="nav-link <c:if test="${menuActive.equals(item.href)}">active</c:if>" href="<c:url value='${item.href}'/>">${item.title}</a>
    </li>
   </c:forEach>
  </ul>
</nav>
<div class= "container">
<c:if test="${brandList == null && typeList == null}">
	<h1>Hello. This is Web crawl data of website thegioididong</h1>
</c:if>
<div class="row">
<c:if test="${typeList != null}">
	
	
    <div class="list-group col-md-4">
    <h4>Loại</h4>
   <c:forEach items="${typeList}" var="item">
    	<a href="<c:url value='${item.href}'/>" class="list-group-item list-group-item-action <c:if test="${typeActive != null}"><c:if test="${item.href.contains(typeActive)}">active</c:if></c:if>">${item.title}</a>
   </c:forEach>
   </div>
</c:if>

<c:if test="${brandList != null}">
	
	<div class="list-group col-md-4">
	<h4>Nhãn hàng</h4>
   <c:forEach items="${brandList}" var="item">
    	<a href="<c:url value='${item.href}'/>" class="list-group-item list-group-item-action">${item.title}</a>
   </c:forEach>
  </div>
	
</c:if>
</div>
</div>

<script type="text/javascript">
	var btn = $('#button-back-top');

	$(window).scroll(function() {
	  if ($(window).scrollTop() > 300) {
	    btn.addClass('show');
	  } else {
	    btn.removeClass('show');
	  }
	});

	btn.on('click', function(e) {
	  e.preventDefault();
	  $('html, body').animate({scrollTop:0}, '300');
	});
	</script>
</body>
</html>