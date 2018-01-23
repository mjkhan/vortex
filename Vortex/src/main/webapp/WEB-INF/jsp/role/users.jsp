<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<div id="users" style="padding:1em 0;">
	<div class="inputArea">
		 <button id="btnAddUsers" onclick="addUsers();" type="button">추가</button>
		 <button id="btnDelUsers" onclick="deleteUsers();" type="button" class="hidden">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleUsers" type="checkbox" /></th>
				<th width="30%">아이디</th>
				<th width="40%">이름</th>
				<th width="20%">등록시간</th>
			</tr>
		</thead>
		<tbody id="userList">
		<c:set var="userNotFound"><tr><td colspan="4" class="notFound">사용자 정보를 찾지 못했습니다.</td></c:set>
		<c:set var="userRow"><tr>
			<td><input name="userID" value="{userID}" type="checkbox" /></td>
				<td>{userID}</td>
				<td>{userName}</td>
				<td>{insTime}</td>
			</tr></c:set>
		</tbody>
	</table>
	<div id="moreUsers" class="paging"></div>
</div>
<vtx:script type="decl">
var checkedUsers;

function getUsers(start) {
	ajax({
		url:"<c:url value='/role/user/list.do'/>",
		data:{
			roleID:currentRole.GRP_ID,
			start:start
		},
		success:setUserList
	});
}

function setUserList(resp) {
	$("#userList").populate({
		data:resp.users,
		tr:function(row){
			return "${vtx:jstring(userRow)}"
				.replace(/{userID}/g, row.USER_ID)
				.replace(/{userName}/g, row.USER_NAME)
				.replace(/{insTime}/g, row.INS_TIME);
		},
		ifEmpty:"${vtx:jstring(userNotFound)}"
	});

	checkedUsers = checkbox("#users input[type='checkbox'][name='userID']")
		.onChange(function(checked){
			if (checked)
				$("#btnDelUsers").fadeIn();
			else
				$("#btnDelUsers").fadeOut();
		});
	checkbox("#toggleUsers")
		.onChange(function(checked){checkedUsers.check(checked);})
		.check(false);
	
	$("#moreUsers").setPaging({
		start:resp.userStart,
		fetchSize:resp.fetch,
		totalSize:resp.totalUsers,
		func:"getUsers({index});"
	});
}

function addUsers(){
	ajax({
		url:"<c:url value='/user/select.do'/>",
		data:{init:true},
		success:function(resp) {
			dialog({
				title:"사용자 선택",
				content:resp,
				onOK:function(selected){
					var userIDs = valuesOf(selected, "USER_ID").join(",");

					ajax({
						url:"<c:url value='/role/user/add.do'/>",
						data:{
							roleID:currentRole.GRP_ID,
							userID:userIDs
						},
						success:function(resp){
							if (!resp.saved)
								return alert("저장되지 않았습니다.");
							getUsers();
						}
					});
				}
			});
		}
	});
}

function deleteUsers(){
	if (!confirm("선택한 사용자를 '" + currentRole.GRP_NAME + "' ROLE에서 삭제하시겠습니까?")) return;
	
	var userIDs = checkedUsers.value();
	ajax({
		url:"<c:url value='/role/user/delete.do'/>",
		data:{
			roleID:currentRole.GRP_ID,
			userID:userIDs.join(","),
		},
		success:function(resp){
			if (!resp.saved)
				return alert("저장되지 않았습니다.");
			getUsers();
		}
	});
}
</vtx:script>
<vtx:script type="docReady">
	setUserList({
		users:<c:if test="${!empty users}"><vtx:json data="${users}" mapper="${objectMapper}"/></c:if><c:if test="${empty users}">[]</c:if>,
		totalUsers:${vtx:ifEmpty(totalUsers, 0)},
		userStart:${vtx:ifEmpty(userStart, -1)},
		fetch:${fetch}
	});
</vtx:script>