<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" session="false" isErrorPage="true"%>
<%@ page import="vortex.support.Assert"%>
<%	//boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	//request.setAttribute("ajax", ajax);
	boolean ajax = Boolean.TRUE.equals(request.getAttribute("ajax"));
	response.setCharacterEncoding("UTF-8");
	
	response.setContentType(!ajax ? "text/html" : "application/json");
	request.setAttribute("path", request.getAttribute("javax.servlet.forward.request_uri"));
	Throwable cause = Assert.rootCause(exception);
	if (cause != null)
		request.setAttribute("error", cause);
%>