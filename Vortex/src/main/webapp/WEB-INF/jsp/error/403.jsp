<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" session="false" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="eheader.jsp"/>
<%	pageContext.setAttribute("title", "페이지 접근 또는 작업 요청이 거부됐습니다.");
%>
<c:if test="${!ajax}">
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div style="width:100%; padding:2em;">
<p>${title}</p>
<p>${path}</p>
</div>
<vtx:script type="docReady">
	docTitle("오류: 403");
	subTitle("권한 오류(403)");
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
</c:if>
<c:if test="${ajax}">
<c:set var="handler">
alert(["${title}", "${path}"].join("\n\n"));
location.reload();
</c:set>{
	"title":"${title}",
	"path":"${path}",
	"status":403,
	"handler":"${vtx:jstring(handler)}"
}</c:if>