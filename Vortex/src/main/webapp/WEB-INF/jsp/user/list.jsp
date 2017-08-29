<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="initial-scale=1.0, width=device-width">
<%-- 
<link rel="shortcut icon" href="<c:url value='asset/images/favicon.ico'/>" type="image/x-icon" />
<link rel="icon" href="<c:url value='/asset/images/favicon.ico'/>" type="image/x-icon" />
 --%>
<title>Vortex</title>
</head>
<body>
<h1>사용자 목록</h1>

<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript">
var wctx = "${pageContext.request.contextPath}";

function setTitle(title) {
	document.title = "Vortex - " + title;
}

$(function(){
	setTitle("사용자 목록");
});
</script>
</body>
</html>