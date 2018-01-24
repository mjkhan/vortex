<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<div id="permissions" class="hidden" style="padding:1em 0;">
	<div class="inputArea">
		 <button id="btnAddPermissions" onclick="addPermissions();" type="button">추가</button>
		 <button id="btnDelPermissions" onclick="deletePermissions();" type="button" class="hidden">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="togglePermissions" type="checkbox" /></th>
				<th width="30%">아이디</th>
				<th width="40%">이름</th>
				<th width="20%">등록시간</th>
			</tr>
		</thead>
		<tbody id="permissionList">
		<c:set var="permissionNotFound"><tr><td colspan="4" class="notFound">권한정보를 찾지 못했습니다.</td></c:set>
		<c:set var="permissionRow"><tr>
			<td><input name="permissionID" value="{permissionID}" type="checkbox" /></td>
				<td>{permissionID}</td>
				<td>{permissionName}</td>
				<td>{insTime}</td>
			</tr></c:set>
		</tbody>
	</table>
	<div id="morePermissions"></div>
</div>
<vtx:script type="decl">
var checkedPermissions;

function getPermissions(start) {
	ajax({
		url:"<c:url value='/role/permission/list.do'/>",
		data:{roleID:currentRole.GRP_ID},
		success:setPermissionList
	});
}

function setPermissionList(resp) {
	$("#permissionList").populate({
		data:resp.permissions,
		tr:function(row){
			return "${vtx:jstring(permissionRow)}"
				.replace(/{permissionID}/g, row.PMS_ID)
				.replace(/{permissionName}/g, row.PMS_NAME)
				.replace(/{insTime}/g, row.INS_TIME);
		},
		ifEmpty:"${vtx:jstring(permissionNotFound)}"
	});
	
	var showOnCheck = function(checked) {
		if (checked)
			$("#btnDelPermissions").fadeIn();
		else
			$("#btnDelPermissions").fadeOut();
	};

	checkedPermissions = checkbox("input[type='checkbox'][name='permissionID']")
		.onChange(showOnCheck);
	checkbox("#togglePermissions")
		.onChange(function(checked){
			checkedPermissions.check(checked);
			showOnCheck(checked);
		})
		.check(false);
	if (resp.totalPermissions < 1)
		showOnCheck(false);
	
	$("#morePermissions").setPaging({
		start:resp.permissionStart,
		fetchSize:resp.fetch,
		totalSize:resp.totalPermissions,
		func:"getPermissions({index});"
	});
}

function addPermissions(){
	ajax({
		url:"<c:url value='/permission/select.do'/>",
		data:{init:true},
		success:function(resp) {
			dialog({
				title:"권한 선택",
				content:resp,
				onOK:function(selected){
					var permissionIDs = valuesOf(selected, "PMS_ID").join(",");
					ajax({
						url:"<c:url value='/role/permission/add.do'/>",
						data:{
							roleID:currentRole.GRP_ID,
							permissionID:permissionIDs,
						},
						success:function(resp){
							if (!resp.saved)
								return alert("이미 등록된 권한입니다.");
							getPermissions();
						}
					});
				}
			});
		}
	});
}

function deletePermissions(){
	if (!confirm("선택한 권한을 '" + currentRole.GRP_NAME + "' ROLE에서 삭제하시겠습니까?")) return;
	
	var permissionIDs = checkedPermissions.value();
	ajax({
		url:"<c:url value='/role/permission/delete.do'/>",
		data:{
			roleID:currentRole.GRP_ID,
			permissionID:permissionIDs.join(","),
		},
		success:function(resp){
			if (!resp.saved)
				return alert("저장되지 않았습니다.");
			getPermissions();
		}
	});
}
</vtx:script>
<vtx:script type="docReady">
	setPermissionList({
		permissions:<c:if test="${!empty permissions}"><vtx:json data="${permissions}" mapper="${objectMapper}"/></c:if><c:if test="${empty permissions}">[]</c:if>,
		totalPermissions:${vtx:ifEmpty(totalPermissions, 0)},
		permissionStart:${vtx:ifEmpty(permissionStart, -1)},
		fetch:${fetch}
	});
</vtx:script>