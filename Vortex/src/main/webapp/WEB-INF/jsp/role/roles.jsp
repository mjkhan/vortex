<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div id="roles" style="width:100%; display:flex; flex-flow:wrap;">
	<div id="roleArea">
		<jsp:include page="/WEB-INF/jsp/role/list.jsp"/>
	</div>
</div>
<div id="roleDetail" class="hidden" style="padding:1em 0;"></div>
<vtx:script type="decl">
function showDetail(show) {
	if (show != false) {
		$("#roles").hide();
		$("#roleDetail").fadeIn();
	} else {
		if (afterSave) {
			afterSave();
			afterSave = null;
		}
		$("#roleDetail").hide();
		$("#roles").fadeIn();
	}
}
</vtx:script>
<vtx:script type="docReady">
	docTitle("ROLE 정보");
	subTitle("ROLE 정보");
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>