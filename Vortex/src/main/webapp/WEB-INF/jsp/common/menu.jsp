<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<sec:authorize access="isAuthenticated()">
<ul class="menu">
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