<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ page import="vortex.application.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html><% request.setAttribute("currentUser", User.current()); %>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="initial-scale=1.0, width=device-width">
<link href="<c:url value='/asset/image/favicon.ico'/>" rel="shortcut icon" type="image/x-icon" />
<link href="<c:url value='/asset/image/favicon.ico'/>" rel="icon" type="image/x-icon" />
<link href="<c:url value='/asset/css/styles.css'/>" rel="stylesheet" type="text/css" />
<c:if test="${!empty css}">${css}</c:if>
<!--[if lt IE 9]><script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script><![endif]-->
<title></title>
</head>
<body>
<img class="wait" src="<c:url value='/asset/image/spinner.gif'/>"/>
<header><c:set var="wctx" scope="request">${pageContext.request.contextPath}</c:set>
	<div id="mainTitle" class="mainTitle"><a href="${wctx}">Vortex</a></div>
	<c:if test="${currentUser.authenticated}">
	<div style="text-align:right; padding-left:.5em; padding-bottom:.5em; color:white; background-color:purple; font-size:90%;">반갑습니다, ${currentUser.name}님(${currentUser.roleNames})</div>
	</c:if>
	<jsp:include page="menu.jsp"/>
	<div id="subTitle" class="subTitle"></div>
</header>
<main>