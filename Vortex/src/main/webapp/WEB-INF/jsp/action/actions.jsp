<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div id="appActions" style="width:100%; display:flex; flex-flow:wrap;">
	<div id="actionGroups">
		<h1 style="padding-top:.5em;">액션 그룹</h1>
		<jsp:include page="/WEB-INF/jsp/action/group-list.jsp"/>
	</div>
	<div id="actions">
		<h1 id="actionGroup" style="padding-top:.5em;"></h1>
		<jsp:include page="/WEB-INF/jsp/action/action-list.jsp"/>
	</div>
</div>
<div id="detailInfo" class="hidden" style="padding:1em 0;"></div>
<vtx:script type="decl">
var afterSave;

function showDetail(show) {
	if (show == false) {
		if (afterSave) {
			afterSave();
			afterSave = null;
		};
		$("#detailInfo").hide();
		$("#appActions").fadeIn();
	} else {
		$("#appActions").hide();
		$("#detailInfo").fadeIn();
	}
}

function setDetail(content) {
	$("#detailInfo").html(content);
	showDetail();
}

function setGroupName(name) {
	$("#actionGroup").html(name);
}
</vtx:script>
<vtx:script type="docReady">
	docTitle("액션 정보");
	subTitle("액션 정보");
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>