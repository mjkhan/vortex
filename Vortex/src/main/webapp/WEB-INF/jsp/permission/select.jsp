<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<c:set var="type">${!empty param.type ? param.type : 'checkbox'}</c:set>
	<div class="inputArea">
		<select id="_field">
			<option value="">검색조건</option>
			<option value="PMS_ID">아이디</option>
			<option value="PMS_NAME">이름</option>
		 </select>
		 <input id="_value" type="search" placeholder="검색어" style="width:40%;"/>
		 <button onclick="_searchPermissions();" type="button">찾기</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="_togglePermissions" type="checkbox" /></th>
				<th width="20%">아이디</th>
				<th width="50%">이름</th>
			</tr>
		</thead>
		<tbody id="_permissionList">
		<c:set var="notFound"><tr><td colspan="3" class="notFound">권한을 찾지 못했습니다.</td></c:set>
		<c:set var="pmsRow"><tr><td><input name="_permissionID" value="{permissionID}" type="checkbox" /></td>
				<td>{permissionID}</td>
				<td>{permissionName}</td>
			</tr></c:set>
		</tbody>
	</table>
	<div id="_morePermissions" class="paging"></div>
<script type="text/javascript">
var userSelection,
	_checkedPermissions;

function _searchPermissions(start) {
	var field = $("#_field").val(),
		value = $("#_value").val();
	if (value && !field)
		return alert("검색조건을 선택하십시오.");
	ajax({
		url:"<c:url value='/permission/list.do'/>"
	   ,data:{
			searchBy:field
		   ,searchTerms:value
		   ,start:start
		}
	   ,success:_setPermissionList
	});
	currentPermissions = function(){search(start);};
}

function _setPermissionList(resp) {
	var permissions = resp.permissions;
	$("#_permissionList").populate({
		data:permissions
	   ,tr:function(row){
	   		var pmsID = row.PMS_ID,
	   			builtin = pmsID == 'all' || pmsID == 'authenticated';
	   		if (builtin) return null;
	   		
			return "${vtx:jstring(pmsRow)}"
				.replace(/{permissionID}/g, pmsID)
				.replace(/{permissionName}/g, row.PMS_NAME)
				.replace(/{updTime}/g, row.UPD_TIME)
				.replace(/{builtin}/g, !builtin ? "" : "builtin=\"true\"");
		}
	   ,ifEmpty:"${vtx:jstring(notFound)}"
	});

	$("#_morePermissions").setPaging({
	    start:resp.permissionStart
	   ,fetchSize:resp.fetch
	   ,totalSize:resp.totalPermissions
	   ,func:"_searchPermissions({index})"
	});
	
	_checkedPermissions = checkbox("input[type='checkbox'][name='_permissionID']")
		.onChange(function(checked){
			if (checked)
				$("#permissions .showOnCheck").fadeIn();
			else
				$("#permissions .showOnCheck").fadeOut();
		});
	checkbox("#_togglePermissions")
		.onChange(function(checked){_checkedPermissions.check(checked);})
		.check(false);

	<c:if test="${'checkbox' == type}">
	userSelection = function() {
		var permissionIDs = checkbox("input[name='_permissionID']").value();
		return !isEmpty(permissionIDs) ?
			elementsOf(permissions, "PMS_ID", permissionIDs) :
			alert("권한을 선택하십시오.");
	};
	checkbox("#_togglePermissions").onChange(function(checked){
		checkbox("input[type='checkbox'][name='_permissionID']").check(checked);
	});
	</c:if>
	<c:if test="${'radio' == type}">
	userSelection = function() {
		var permissionID = $("input[name='_permissionID']:checked").val();
		return !isEmpty(permissionID) ?
			elementsOf(permissions, "USER_ID", permissionID)[0] :
			alert("권한을 선택하십시오.");
	};
	</c:if>
}

$(function(){
	$("#searchTerms").onEnterPress(_searchPermissions);
	_setPermissionList({
		permissions:<vtx:json data="${permissions}" mapper="${objectMapper}"/>
	   ,totalPermissions:${totalPermissions}
	   ,permissionStart:${permissionStart}
	   ,fetch:${fetch}
	});
});
</script>
