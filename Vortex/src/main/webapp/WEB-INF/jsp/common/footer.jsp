<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
</main>
<footer>
</footer>
<script src="<c:url value='/asset/js/jquery-3.2.1.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/asset/js/base.js'/>" type="text/javascript"></script>
<script src="<c:url value='/asset/js/page.js'/>" type="text/javascript"></script>
<script src="<c:url value='/asset/js/vortex.js'/>" type="text/javascript"></script>
<vtx:script type="src" write="true"/>
<script type="text/javascript">
var wctx = {
		path:"${pageContext.request.contextPath}",
		url:function(path) {
			return this.path + path;
		},
		debug:${debug}
	},
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

<vtx:script type="decl" write="true"/>

$(function(){
	<vtx:script type="docReady" write="true"/>
	debug("Hello, world!");
});
</script>
</body>
</html>