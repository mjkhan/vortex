<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<c:if test="${'/user/login.do' != client.action}">
<ul class="menu"><c:forEach items="${topMenus}" var="menu">
	<vtx:granted menu="${menu}"><li${currentMenu ? ' class="current"' : ''}><a<c:if test="${!currentMenu}"> href="<c:url value='${menuAction}'/>"</c:if>>${menuName}</a></li></vtx:granted></c:forEach>
	<li><a onclick="logout();">로그아웃</a></li>
</ul>
<vtx:script type="decl">
function logout() {
	if (!confirm("로그아웃 하시겠습니까?")) return;

	var form = $("<form action=\"<c:url value='/logout'/>\", method=\"POST\">");
	$("<input name=\"${_csrf.parameterName}\" value=\"${_csrf.token}\" type=\"hidden\">").appendTo(form);
	form.appendTo("body").submit();
}
</vtx:script>
</c:if>