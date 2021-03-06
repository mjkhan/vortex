<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" session="false" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="eheader.jsp"/>
<%	pageContext.setAttribute("title", "요청수행 중 오류가 발생했습니다.");
	Throwable cause = (Throwable)request.getAttribute("error");
	if (cause != null) {
		pageContext.setAttribute("name", cause.getClass().getName());
		pageContext.setAttribute("message", cause.getMessage());
	}
%>
<c:if test="${!ajax}">
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div style="width:100%; padding:2em;">
	<p>${path}</p>
	<p>${title}</p>
	<p>${name}</p>
	<p>${message}</p>
</div>
<vtx:script type="docReady">
	docTitle("서버 오류");
	subTitle("서버 오류");
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
</c:if>
<c:if test="${ajax}">{
	"path":"${path}",
	"title":"${title}",
	"name":"${name}",
	"message":"${vtx:jstring(message)}",
	"status":500
}</c:if>