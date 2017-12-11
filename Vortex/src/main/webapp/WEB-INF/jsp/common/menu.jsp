<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils, vortex.application.menu.service.MenuService, vortex.application.menu.MenuContext"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<%	MenuService service = (MenuService)WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()).getBean("menuService");
	MenuContext mctx = service.getMenuCotext();
	pageContext.setAttribute("tops", mctx.getMenus(0));
%>
<sec:authorize access="isAuthenticated()">
<ul class="menu">
	<c:forEach items="${tops}" var="menu">
	<li><a href="/vortex${menu.action}">${menu.name}</a></li>
	</c:forEach>
	<li><a onclick="logout();">로그아웃</a></li>
</ul>
<vtx:script type="decl">
function logout() {
	if (!confirm("로그아웃 하시겠습니까?")) return;

	var form = $("<form>").attr("action", "<c:url value='/logout'/>").attr("method", "post");
	$("<input>").attr("name", "${_csrf.parameterName}").val("${_csrf.token}").attr("type", "hidden").appendTo(form);
	form.appendTo("body").submit();
}
</vtx:script>
</sec:authorize>