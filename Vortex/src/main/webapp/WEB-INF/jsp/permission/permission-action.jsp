<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div id="permission-actions" style="width:100%; display:flex; flex-flow:wrap;">
	<div id="permissions">
		<h1 style="padding-top:.5em;">권한 목록</h1>
		<jsp:include page="/WEB-INF/jsp/permission/list.jsp"/>
	</div>
	<div id="actions">
		<h1 id="pmsName" style="padding-top:.5em;"></h1>
		<jsp:include page="/WEB-INF/jsp/permission/action-list.jsp"/>
	</div>
</div>
<div id="detailInfo" class="hidden" style="padding:1em 0;"></div>
<vtx:script type="decl">
function showDetail(show) {
	if (show == false) {
		if (afterSave) {
			afterSave();
			afterSave = null;
		};
		$("#detailInfo").hide();
		$("#permission-actions").fadeIn();
	} else {
		$("#permission-actions").hide();
		$("#detailInfo").fadeIn();
	}
}

function setDetail(content) {
	$("#detailInfo").html(content);
	showDetail();
}

function setPermissionName(name) {
	$("#pmsName").html(name);
}
</vtx:script>
<vtx:script type="docReady">
	docTitle("권한 정보");
	subTitle("권한 정보");
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>