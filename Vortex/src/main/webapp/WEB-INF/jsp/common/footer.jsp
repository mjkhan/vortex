<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<sec:authorize access="isAuthenticated()"><button onclick="logout();" type="button">로그아웃</button></sec:authorize>
</main>
<footer>
</footer>
<script type="text/javascript" src="<c:url value='/asset/js/jquery-3.2.1.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/asset/js/base.js'/>"></script>
<script type="text/javascript" src="<c:url value='/asset/js/page.js'/>"></script>
<vtx:script type="src" write="true"/>
<script type="text/javascript">
var wctx = "${pageContext.request.contextPath}",
	csrf = {
		header:"${_csrf.headerName}",
		token:"${_csrf.token}"
	};
function docTitle(title) {
	document.title = title ? "Vortex - " + title : "Vortex";
}

function subTitle(title) {
	$("#subTitle").html(title);
}
<sec:authorize access="isAuthenticated()">
function logout() {
	var form = $("<form>").attr("action", "<c:url value='/logout'/>").attr("method", "post");
	$("<input>").attr("name", "${_csrf.parameterName}").val("${_csrf.token}").attr("type", "hidden").appendTo(form);
	form.appendTo("body").submit();
}
</sec:authorize>
<vtx:script type="decl" write="true"/>

$(function(){
	<vtx:script type="docReady" write="true"/>
});
</script>
</body>
</html>