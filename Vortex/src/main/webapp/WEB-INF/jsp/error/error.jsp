<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" session="false" isErrorPage="true"%>
<%@ page import="vortex.support.Assert"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<%	boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	response.setContentType(!ajax ? "text/html; charset=UTF-8" : "application/json; charset=UTF-8");
	pageContext.setAttribute("ajax", ajax);
	Throwable cause = Assert.rootCause(exception);
	pageContext.setAttribute("title", "요청수행 중 오류가 발생했습니다.");
	pageContext.setAttribute("name", cause.getClass().getName());
	pageContext.setAttribute("message", cause.getMessage());
%>
<c:if test="${!ajax}">
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div style="width:100%; padding:2em;">
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
<c:if test="${ajax}">
{
	"title":"${title}",
	"name":"${name}",
	"message":"${message}"
}
</c:if>