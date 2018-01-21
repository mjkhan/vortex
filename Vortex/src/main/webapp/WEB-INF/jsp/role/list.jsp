<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<h1 style="padding-top:.5em;">ROLE 목록</h1>
<div class="inputArea">
	<select id="field">
		<option value="">검색조건</option>
		<option value="GRP_ID">아이디</option>
		<option value="GRP_NAME">이름</option>
	 </select>
	 <input id="value" type="search" placeholder="검색어" style="width:40%;"/>
	 <button onclick="search();" type="button">찾기</button>
	 <button onclick="getInfo();" type="button" class="add">추가</button>
	 <button onclick="removeRoles();" type="button" class="showOnCheck">삭제</button>
</div>
<table class="infoList">
	<thead>
		<tr><th width="10%"><input id="toggleChecks" type="checkbox" /></th>
			<th width="20%">아이디</th>
			<th width="50%">이름</th>
			<th width="20%">수정시간</th>
		</tr>
	</thead>
	<tbody id="roleList">
	<c:set var="notFound"><tr><td colspan="4" class="notFound">ROLE을 찾지 못했습니다.</td></c:set>
	<c:set var="roleRow"><tr>
		<td><input name="roleID" value="{roleID}" type="checkbox" /></td>
			<td><a onclick="getInfo('{roleID}')">{roleID}</a></td>
			<td><a onclick="">{roleName}</a></td>
			<td>{updTime}</td>
		</tr></c:set>
	</tbody>
</table>
<div class="paging"></div>
<div style="display:flex;">
	<div id="roleName" style="padding:.5em .5em 0 0;"></div>
	<ul class="tab">
		<li id="userTab" class="current" onclick="getUsers();">사용자</li>
		<li id="permissionTab" onclick="getPermissions();">권한</li>
	</ul>
</div>
<vtx:script type="decl">
var getRole,
	currentRole,
	checkedRoles,
	currentRoles,
	afterSave;

function search(start) {
	var field = $("#field").val(),
		value = $("#value").val();
	if (value && !field)
		return alert("검색조건을 선택하십시오.");
	ajax({
		url:"<c:url value='/role/list.do'/>",
		data:{
			searchBy:field,
			searchTerms:value,
			start:start || 0
		},
		success:function(resp) {
			setRoleList(resp);
		}
	});
	currentRoles = function(){search(start);};
}

function removeRoles() {
	if (!confirm("선택한 ROLE을 삭제하시겠습니까?")) return;

	ajax({
		url:"<c:url value='/role/delete.do'/>",
		data:{roleID:checkedRoles.values().join(",")},
		success:function(resp) {
			if (resp.saved) {
				currentRoles();
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}

function getInfo(roleID) {
	ajax({
		url:"<c:url value='/role/info.do'/>",
		data:{roleID:roleID},
		success:function(resp) {
			$("#roleDetail").html(resp);
			showDetail();
		}
	});
}

function setRoleName(name) {
	$("#roleName").html(name);
}

function setRoleList(resp) {
	getRole = function(roleID) {
		var roles = resp.roles;
		if (!roles || roles.length < 1) return {};
		if (!roleID) return roles[0];
		var found = elementsOf(roles, "GRP_ID", groupID);
		return found ? found[0] : {};
	};
	currentRole = getRole();
	setRoleName(currentRole.GRP_NAME);
	
	$("#roleList").populate({
		data:resp.roles,
		tr:function(row){
			return "${vtx:jstring(roleRow)}"
				.replace(/{roleID}/g, row.GRP_ID)
				.replace(/{roleName}/g, row.GRP_NAME)
				.replace(/{updTime}/g, row.UPD_TIME);
		},
		ifEmpty:"${vtx:jstring(notFound)}"
	});

	$("#roleArea .showOnCheck").fadeOut();
	$("#roleArea .paging").setPaging({
	    start:resp.roleStart
	   ,fetchSize:resp.fetch
	   ,totalSize:resp.totalRoles
	   ,func:"window.search({index})"
	});
	
	checkedRoles = checkbox("input[type='checkbox'][name='roleID']")
		.onChange(function(checked){
			if (checked)
				$("#roleArea .showOnCheck").fadeIn();
			else
				$("#roleArea .showOnCheck").fadeOut();
		});
	checkbox("#roleArea #toggleChecks").onChange(function(checked){checkedRoles.check(checked);});
}

function getUsers(start) {
	$(".tab li").removeClass("current");
	$("#userTab").addClass("current");
	currentMembers = function() {getUsers(start);};
}

function getPermissions(start) {
	$(".tab li").removeClass("current");
	$("#permissionTab").addClass("current");
	currentMembers = function() {getPermissions(start);};
}

var currentMembers = getUsers;
</vtx:script>
<vtx:script type="docReady">
$("#value").onEnterPress(search);
setRoleList({
	roles:<vtx:json data="${roles}" mapper="${objectMapper}"/>,
	totalRoles:${totalRoles},
	roleStart:${roleStart},
	fetch:${fetch}
});
currentRoles = search;
</vtx:script>