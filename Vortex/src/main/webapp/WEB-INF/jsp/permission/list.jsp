<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
	<div class="inputArea">
		<select id="searchBy">
			<option value="">검색조건</option>
			<option value="PMS_ID">아이디</option>
			<option value="PMS_NAME">이름</option>
		 </select>
		 <input id="searchTerms" type="search" placeholder="검색어" style="width:40%;"/>
		 <button onclick="search();" type="button">찾기</button>
		 <button onclick="getInfo();" type="button" class="add">추가</button>
		 <button onclick="remove();" type="button" class="showOnCheck">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleChecks" type="checkbox" /></th>
				<th width="20%">아이디</th>
				<th width="50%">이름</th>
				<th width="20%">등록시간</th>
			</tr>
		</thead>
		<tbody id="permissionList">
		<c:set var="notFound"><tr><td colspan="4" class="notFound">권한을 찾지 못했습니다.</td></c:set>
		<c:set var="pmsRow"><tr {builtin}>
				<td><input name="permissionID" value="{permissionID}" type="checkbox" /></td>
				<td><a onclick="getInfo('{permissionID}')" title="권한정보 보기">{permissionID}</a></td>
				<td><a onclick="getActionsOf('{permissionID}')" title="액션목록 보기">{permissionName}</a></td>
				<td>{updTime}</td>
			</tr></c:set>
		</tbody>
	</table>
	<div class="paging"></div>
<vtx:script type="decl">
var getPermission,
	currentPermission,
	checkedPermissions,
	currentPermissions,
	afterSave;

function search(start) {
	var field = $("#searchBy").val(),
		value = $("#searchTerms").val();
	if (value && !field)
		return alert("검색조건을 선택하십시오.");
	ajax({
		url:"<c:url value='/permission/list.do'/>"
	   ,data:{
			searchBy:field
		   ,searchTerms:value
		   ,start:start
		}
	   ,success:setPermissionList
	});
	currentPermissions = function(){search(start);};
}

function remove() {
	if (!confirm("선택한 권한을 삭제하시겠습니까?")) return;

	ajax({
		url:"<c:url value='/permission/delete.do'/>",
		data:{permissionID:checkedPermissions.values().join(",")},
		success:function(resp) {
			if (resp.saved) {
				currentPermissions();
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}

function getInfo(permissionID) {
	ajax({
		url:"<c:url value='/permission/info.do'/>",
		data:{permissionID:permissionID},
		success:setDetail
	});
}

function setPermissionList(resp) {
	getPermission = function(permissionID) {
		var permissions = resp.permissions;
		if (!permissions || permissions.length < 1) return {};
		if (!permissionID) return permissions[0];
		var found = elementsOf(permissions, "PMS_ID", permissionID);
		return found ? found[0] : {};
	};
	currentPermission = getPermission();
	setPermissionName(currentPermission.PMS_NAME);
	
	$("#permissionList").populate({
		data:resp.permissions
	   ,tr:function(row){
	   		var pmsID = row.PMS_ID,
	   			builtin = pmsID == 'all' || pmsID == 'authenticated';
			return "${vtx:jstring(pmsRow)}"
				.replace(/{permissionID}/g, pmsID)
				.replace(/{permissionName}/g, row.PMS_NAME)
				.replace(/{updTime}/g, row.UPD_TIME)
				.replace(/{builtin}/g, !builtin ? "" : "builtin=\"true\"");
		}
	   ,ifEmpty:"${vtx:jstring(notFound)}"
	});
	
	var builtin = $("#permissionList tr[builtin='true']");
	builtin.find("input[name='permissionID']").hide();
	builtin.find("td a[onclick^='getInfo']").removeAttr("onclick");

	$("#permission-actions .paging").setPaging({
	    start:resp.permissionStart
	   ,fetchSize:resp.fetch
	   ,totalSize:resp.totalPermissions
	   ,func:"search({index})"
	});
	
	checkedPermissions = checkbox("input[type='checkbox'][name='permissionID']")
		.onChange(function(checked){
			if (checked)
				$("#permissions .showOnCheck").fadeIn();
			else
				$("#permissions .showOnCheck").fadeOut();
		});
	checkbox("#permissions #toggleChecks")
		.onChange(function(checked){checkedPermissions.check(checked);})
		.check(false);
}

function getActionsOf(permissionID) {
	currentPermission = getPermission(permissionID);
	setPermissionName(currentPermission.PMS_NAME);
	getActions();
}
</vtx:script>
<vtx:script type="docReady">
	$("#searchTerms").onEnterPress(search);
	setPermissionList({
		permissions:<vtx:json data="${permissions}" mapper="${objectMapper}"/>
	   ,totalPermissions:${totalPermissions}
	   ,permissionStart:${permissionStart}
	   ,fetch:${fetch}
	   ,actions:<vtx:json data="${actions}" mapper="${objectMapper}"/>
	});
	currentPermissions = search;
</vtx:script>