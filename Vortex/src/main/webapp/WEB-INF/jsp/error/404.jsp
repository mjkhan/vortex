<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" session="false" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="eheader.jsp"/>
<%	pageContext.setAttribute("title", "알 수 없는 요청입니다.");
	pageContext.setAttribute("msg", "요청 URL이 올바른지 확인하십시오.");
%>
<c:if test="${!ajax}">
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div style="width:100%; padding:2em;">
<p>${title}</p>
<p>${path}</p>
<c:if test="${debug}"><p>${msg}</p></c:if>
</div>
<vtx:script type="docReady">
	docTitle("오류: 404");
	subTitle("404 오류");
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
</c:if>
<c:if test="${ajax}">{
	"title":"${title}",
	"path":"${path}",
	<c:if test="${debug}">"msg":"${msg}",</c:if>
	"status":404,
	"handler":"alert('${title}\\n\\n${path}<c:if test="${debug}">\\n\\n${msg}</c:if>');"
}</c:if>