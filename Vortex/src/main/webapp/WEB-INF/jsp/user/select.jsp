<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<c:set var="type">${!empty param.type ? param.type : 'checkbox'}</c:set>
<div class="inputArea">
	<select id="_field">
		<option value="">검색조건</option>
		<option value="USER_ID">아이디</option>
		<option value="USER_NAME">이름</option>
		<option value="ALIAS">별명</option>
	</select>
	<input id="_value" type="search" placeholder="검색어" style="width:40%;"/>
	<button onclick="_searchUsers();" type="button">찾기</button>
</div>
<table class="infoList">
	<thead>
		<tr><th width="10%">
				<c:if test="${'checkbox' == type}"><input id="_toggleUsers" type="${type}" /></c:if>
				<c:if test="${'radio' == type}">선택</c:if>
			</th>
			<th width="20%">아이디</th>
			<th width="30%">이름</th>
			<th width="20%">별명</th>
		</tr>
	</thead>
	<tbody id="_userList">
		<c:set var="notFound"><tr><td colspan="4" class="notFound">사용자를 찾지 못했습니다.</td></c:set>
		<c:set var="userRow"><tr>
			<td><input name="_userID" value="{userID}" type="${type}" /></td>
			<td>{userID}</td>
			<td>{userName}</td>
			<td>{alias}</td>
		</tr></c:set>
	</tbody>
</table>
<div class="paging"></div>
<script type="text/javascript">
var userSelection;

function _searchUsers(start) {
	var field = $("#_field").val(),
		value = $("#_value").val();
	if (value && !field)
		return alert("검색조건을 선택하십시오.");
	ajax({
		url:"<c:url value='/user/select.do'/>",
		data:{
			field:field,
			value:value,
			start:start || 0
		},
		success:_setUsers
	});
}

function _setUsers(resp) {
	var users = resp.users;
	$("#_userList").populate({
		data:users,
		tr:function(row){
			return "${vtx:jstring(userRow)}"
				.replace(/{userID}/g, row.USER_ID)
				.replace(/{userName}/g, row.USER_NAME)
				.replace(/{alias}/g, row.ALIAS);
		},
		ifEmpty:"${vtx:jstring(notFound)}"
	});
	
	$(".paging").setPaging({
		start:resp.start,
		fetchSize:resp.fetchSize,
		totalSize:resp.totalSize,
		func:"_searchUsers({index})"
	});
	<c:if test="${'checkbox' == type}">
	userSelection = function() {
		var userIDs = checkbox("input[name='_userID']").value();
		return !isEmpty(userIDs) ?
			elementsOf(users, "USER_ID", userIDs) :
			alert("사용자를 선택하십시오.");
	};
	checkbox("#_toggleUsers").onChange(function(checked){
		checkbox("input[type='checkbox'][name='_userID']").check(checked);
	});
	</c:if>
	<c:if test="${'radio' == type}">
	userSelection = function() {
		var userID = $("input[name='_userID']:checked").val();
		return !isEmpty(userID) ?
			elementsOf(users, "USER_ID", userID)[0] :
			alert("사용자를 선택하십시오.");
	};
	</c:if>
}

$(function(){
	_setUsers({
		users:<vtx:json data="${users}" mapper="${objectMapper}"/>,
		start:0,
		fetchSize:${fetchSize},
		totalSize:${totalSize}
	});
});
</script>