<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" session="false" isErrorPage="true"%>
<%@ page import="vortex.support.Assert"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<%	boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	response.setContentType(!ajax ? "text/html; charset=UTF-8" : "application/json; charset=UTF-8");
	pageContext.setAttribute("ajax", ajax);
	Throwable cause = Assert.rootCause(exception);
	pageContext.setAttribute("title", "요청하신 페이지에 접근할 수 없습니다.");
%>
<c:if test="${!ajax}">
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div style="width:100%; padding:2em;">
<p>${title}</p>
<p>${requestScope['javax.servlet.forward.request_uri']}</p>
</div>
<vtx:script type="docReady">
	docTitle("오류: 403");
	subTitle("403 오류");
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
</c:if>
<c:if test="${ajax}">{
	"title":"${title}",
	"path":"${requestScope['javax.servlet.forward.request_uri']}"
}</c:if>