<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>CRAWL WEB TGDD</title>
<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
  
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

 
#download-file {
  display: block;
  background-color: #1d9d74;
  width: 50px;
  height: 50px;
  text-align: center;
  border-radius: 4px;
  position: fixed;
  top: 150px;
  right: 30px;
  z-index: 1000;
}

#download-file::after {
  content: "\f019";
  font-family: FontAwesome;
  font-weight: normal;
  font-style: normal;
  font-size: 2em;
  line-height: 50px;
  color: #fff;
}

#icon-dowloading {
  position: relative;
  animation-name: example;
  animation-duration: 1s;
  animation-iteration-count: infinite;
  padding: 20px;
  font-size: 40px;
}

@keyframes example {
  0%   {left:0px; top:-5px;}
  50%  { left:0px; top:10px;}
  100% {left:0px; top:-5px;}
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
      <a class="nav-link" href="<c:url value='${item.href}'/>">${item.title}</a>
    </li>
   </c:forEach>
  </ul>
</nav>
	<div class="container">
		<div class="row">
		
		<c:set var="tempLinkSp" value=""/>
		<c:forEach items="${productList}" var="item">
			<div class="col-md-3">=
					<div class="card" style="width:100%">
					  <img class="card-img-top" src="${item.urlImage }" alt="Card image">
					  <div class="card-body">
					    <h6 class="card-title">${item.title }</h6>
					    <strong>${item.price}</strong>  
					  </div>
					</div>
			</div>
		</c:forEach>
		<input type="hidden" value="${tempLinkSp}" id="ip-link-sp">
		</div>
	</div>
	<a href="#" id="download-file" title="Lưu thành file" data-toggle="modal" data-target="#myModal" data-backdrop="static" data-keyboard="false"></a>
	
	<!-- The Modal -->
  <div class="modal" id="myModal">
    <div class="modal-dialog">
      <div class="modal-content">
        
        <!-- Modal body -->
        <div class="modal-body text-center">
          <button class="btn btn-success w-100">
		    
		    <h4><span class="spinner-border spinner-border-lg mr-2"></span> Đang tải... Đợi xíu.</h4>
		  </button>
		  <i class="fa fa-download" aria-hidden="true" id="icon-dowloading"></i>
        </div>
       
        
      </div>
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
	
	function downloadFile() {
		 var xhr = new XMLHttpRequest();
		 xhr.open('GET', "<c:url value='/download?dl='/>${linkOnWebTgdd}", true);
		 xhr.responseType = 'arraybuffer';
		 xhr.onload = function () {
		  if (this.status === 200) {
			  $('#myModal').modal('hide');
		   var filename = "";
		   var disposition = xhr.getResponseHeader('Content-Disposition');
		   if (disposition && disposition.indexOf('attachment') !== -1) {
		    var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
		    var matches = filenameRegex.exec(disposition);
		    if (matches != null && matches[1]) {
		     filename = matches[1].replace(/['"]/g, '');
		    }
		   }
		   var type = xhr.getResponseHeader('Content-Type');
		var blob = typeof File === 'function'
		    ? new File([this.response], filename, { type: type })
		    : new Blob([this.response], { type: type });
		   if (typeof window.navigator.msSaveBlob !== 'undefined') {
		    // IE workaround for "HTML7007: One or more blob URLs were revoked by closing the blob for which they were created. 
		    // These URLs will no longer resolve as the data backing the URL has been freed."
		    window.navigator.msSaveBlob(blob, filename);
		   } else {
		    var URL = window.URL || window.webkitURL;
		    var downloadUrl = URL.createObjectURL(blob);
		if (filename) {
		     // use HTML5 a[download] attribute to specify filename
		     var a = document.createElement("a");
		     // safari doesn't support this yet
		     if (typeof a.download === 'undefined') {
		      window.location = downloadUrl;
		     } else {
		      a.href = downloadUrl;
		      a.download = filename;
		      document.body.appendChild(a);
		      a.click();
		     }
		    } else {
		     window.location = downloadUrl;
		    }
		    setTimeout(function () { URL.revokeObjectURL(downloadUrl); }, 100); // cleanup
		   }
		  }
		 };
		 xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
		 xhr.send($.param({
			
		 }));
		
		}
	
	$("#download-file").click(function (){
		downloadFile();
		 
	});
	</script>
</body>

</html>