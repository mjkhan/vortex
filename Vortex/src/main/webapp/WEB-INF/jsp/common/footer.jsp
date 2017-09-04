<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
</main>
<footer>
</footer>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript" src="<c:url value='/asset/js/base.js'/>"></script>
<script type="text/javascript" src="<c:url value='/asset/js/page.js'/>"></script>
${jsSrc}
<script type="text/javascript">
var wctx = "${pageContext.request.contextPath}";
${jsVar}

function docTitle(title) {
	document.title = title ? "Vortex - " + title : "Vortex";
}

function mainTitle(title) {
	$(".mainTitle").html(title);
}

function subTitle(title) {
	$(".subTitle").html(title);
}

${jsFunc}

$(function(){
	${docReady}
});
</script>
</body>
</html>