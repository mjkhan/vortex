<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div id="common-codes" style="width:100%; display:flex; flex-flow:wrap;">
	<div id="codeGroups">
		<h1 style="padding-top:.5em;">코드 그룹</h1>
		<jsp:include page="/WEB-INF/jsp/code/group/list.jsp"/>
	</div>
	<div id="codes">
		<h1 id="codeGroup" style="padding-top:.5em;"></h1>
		<jsp:include page="/WEB-INF/jsp/code/list.jsp"/>
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
		$("#common-codes").fadeIn();
	} else {
		$("#common-codes").hide();
		$("#detailInfo").fadeIn();
	}
}

function setDetail(content) {
	$("#detailInfo").html(content);
	showDetail();
}

function setGroupName(name) {
	$("#codeGroup").html(name);
}
</vtx:script>
<vtx:script type="docReady">
	docTitle("공통 코드");
	subTitle("공통 코드");
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>