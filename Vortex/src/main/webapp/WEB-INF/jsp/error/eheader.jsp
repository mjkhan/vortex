<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" session="false" isErrorPage="true"%>
<%@ page import="vortex.support.Assert"%>
<%	boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	response.setContentType(!ajax ? "text/html; charset=UTF-8" : "application/json; charset=UTF-8");
	request.setAttribute("ajax", ajax);
	request.setAttribute("path", request.getAttribute("javax.servlet.forward.request_uri"));
	Throwable cause = Assert.rootCause(exception);
	if (cause != null)
		request.setAttribute("error", cause);
%>