<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
</main>
<footer>
</footer>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript" src="<c:url value='/asset/js/base.js'/>"></script>
<script type="text/javascript" src="<c:url value='/asset/js/page.js'/>"></script>
<vtx:script type="src" write="true"/>
<script type="text/javascript">
var wctx = "${pageContext.request.contextPath}";
<%-- 
	csrf = {
		header:"${_csrf.headerName}",
		token:"${_csrf.token}"
	};
--%>
function docTitle(title) {
	document.title = title ? "Vortex - " + title : "Vortex";
}

function subTitle(title) {
	$(".subTitle").html(title);
}

<vtx:script type="decl" write="true"/>

$(function(){
	<vtx:script type="docReady" write="true"/>
});
</script>
</body>
</html>