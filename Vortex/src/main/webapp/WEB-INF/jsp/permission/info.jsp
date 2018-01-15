<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="create">${empty permission}</c:set>
<table class="infoForm">
<c:if test="${!create}">
	<tr><th><label for="permissionID">아이디</label></th>
		<td><input id="permissionID" value="${permission.PMS_ID}" type="text" readonly/></td>
	</tr>
</c:if>
	<tr><th><label for="permissionName">이름</label></th>
		<td><input id="permissionName" value="${permission.PMS_NAME}" type="text" required maxlength="64" /></td>
	</tr>
	<tr><th><label for="actionGroup">액션그룹</label><img onclick="setActionGroup();" alt="액션그룹 선택" title="액션그룹 선택" src="<c:url value='/asset/image/search.png'/>" style="width:15px; height:15px; margin-left:.5em;"></th>
		<td><input id="actionGroup" value="${permission.GRP_ID}" type="hidden" />
			<span id="groupName" >${permission.GRP_NAME}</span>
		</td>
	</tr>
	<tr><th><label for="descrption">설명</label></th>
		<td><textarea id="description" rows="5" style="width:100%; line-height:2em;">${permission.DESCRP}</textarea>
		</td>
	</tr>
<c:if test="${!create}">
	<tr><th>등록자</th>
		<td>${permission.INS_ID}</td>
	</tr>
	<tr><th>등록시간</th>
		<td><fmt:formatDate value="${permission.INS_TIME}" pattern="yy-MM-dd HH:mm"/></td>
	</tr>
	<tr><th>수정자</th>
		<td>${permission.UPD_ID}</td>
	</tr>
	<tr><th>수정시간</th>
		<td><fmt:formatDate value="${permission.UPD_TIME}" pattern="yy-MM-dd HH:mm"/></td>
	</tr>
</c:if>
</table>
<div class="inputArea">
	<button onclick="savePermission();" type="button">저장</button>
	<button onclick="showDetail(false);" type="button">닫기</button>
</div>
<script type="text/javascript">
function setActionGroup() {
	selectGroup({
		title:"액션그룹 선택"
	   ,item:"액션그룹"
	   ,groupType:"002"
	   ,onOK:function(selected) {
			$("#actionGroup").val(selected.GRP_ID);
			$("#groupName").val(selected.GRP_NAME);
	   }
	});
}

function savePermission() {
	if (requiredEmpty()) return;
	
	ajax({
		url:"<c:if test='${create}'><c:url value='/code/group/create.do'/></c:if><c:if test='${!create}'><c:url value='/code/group/update.do'/></c:if>",
		data:{
			id:$("#permissionID").val(),
			name:$("#permissionName").val(),
			description:$("#description").val()
		},
		success:function(resp) {
			if (resp.saved) {
				<c:if test='${create}'>afterSave = searchPermissions;</c:if>
				<c:if test='${!create}'>afterSave = currentPermissions;</c:if>
				alert("저장됐습니다.");
				getInfo(resp.permissionID || $("#permissionID").val());
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}
$(function(){
	$("#permissionName").focus();
	$(".infoForm input:not([readonly])").onEnterPress(savePermission);
});
</script>