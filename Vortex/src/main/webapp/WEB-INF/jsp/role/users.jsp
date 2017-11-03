<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div style="width:100%; margin-top:.1em;">
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleRoles" type="checkbox" /></th>
				<th width="30%">아이디</th>
				<th width="60%">이름</th>
			</tr>
		</thead>
		<tbody id="roleList">
		<c:set var="notFound"><tr><td colspan="3" class="notFound">ROLE을 찾지 못했습니다.</td></c:set>
		<c:set var="roleRow"><tr>
			<td><input name="roleID" value="{roleID}" type="checkbox" /></td>
				<td><a onclick="getUsers('{roleID}')">{roleID}</a></td>
				<td>{roleName}</td>
			</tr></c:set>
		</tbody>
	</table>
</div>
<div id="roleUsers" style="padding:1em 0;">
	<div id="userTitle" class="subTitle">사용자 정보</div>
	<div class="inputArea">
		 <button id="btnAdd" onclick="addUsers();" type="button" class="add hidden">추가</button>
		 <button id="btnRemove" onclick="deleteUsers();" type="button" class="hidden">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleUsers" type="checkbox" /></th>
				<th width="30%">아이디</th>
				<th width="40%">이름</th>
				<th width="20%">등록</th>
			</tr>
		</thead>
		<tbody id="userList">
		<c:set var="userNotFound"><tr><td colspan="4" class="notFound">사용자정보를 찾지 못했습니다.</td></c:set>
		<c:set var="userRow"><tr>
			<td><input name="userID" value="{userID}" type="checkbox" /></td>
				<td>{userID}</td>
				<td>{userName}</td>
				<td>{insTime}</td>
			</tr></c:set>
		</tbody>
	</table>
</div>
<vtx:script type="decl">
var checkedRoles,
	checkedUsers,
	currentUsers;

function setUserTitle(roleID) {
	$("#userTitle").html(roleID + " 사용자 정보");
}

function setRoles(resp) {
	$("#roleList").populate({
		data:resp.roles,
		tr:function(row){
			return "${vtx:jstring(roleRow)}"
				.replace(/{roleID}/g, row.ROLE_ID)
				.replace(/{roleName}/g, row.ROLE_NAME);
		},
		ifEmpty:"${vtx:jstring(notFound)}"
	});

	checkedRoles = checkbox("input[type='checkbox'][name='roleID']")
		.onChange(function(checked){
			if (checked)
				$("#btnAdd").fadeIn();
			else {
				$("#btnAdd").fadeOut();
				checkbox("#toggleUsers").check(false);
			}
		});
	checkbox("#toggleRoles").onChange(function(checked){
		checkedRoles.check(checked);
	});
}

function getUsers(roleID) {
	ajax({
		url:"<c:url value='/role/user/list.do'/>",
		data:{roleID:roleID},
		success:setUsers
	});
	setUserTitle(roleID);
	currentUsers = function(){getUsers(roleID);};
}

function setUsers(resp) {
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

	$("#btnRemove").fadeOut();
	checkedUsers = checkbox("input[type='checkbox'][name='userID']")
		.onChange(function(checked){
			if (checked) {
				if (checkedRoles.isChecked())
					$("#btnRemove").fadeIn();
			} else
				$("#btnRemove").fadeOut();
		});
	checkbox("#toggleUsers").onChange(function(checked){checkedUsers.check(checked);});
}

function addUsers(){
	ajax({
		url:"<c:url value='/user/select.do'/>",
		data:{init:true},
		success:function(resp) {
			dialog.show({
				title:"사용자 선택",
				content:resp,
				onOK:function(){
					var userIDs = userInfo.checked.value();
					if (!userIDs)
						return alert("사용자를 선택하십시오.");
					
					dialog.close();
					ajax({
						url:"<c:url value='/role/user/add.do'/>",
						data:{
							roleIDs:checkedRoles.value().join(","),
							userIDs:userIDs.join(","),
						},
						success:function(resp){
							if (!resp.saved)
								return alert("저장되지 않았습니다.");
							if (currentUsers)
								currentUsers();
							else
								location.reload();
						}
					});
				}
			});
		}
	});
}

function deleteUsers(){
	if (!confirm("선택한 사용자를 ROLE에서 삭제하시겠습니까?")) return;
	
	var userIDs = checkedUsers.value();
	ajax({
		url:"<c:url value='/role/user/delete.do'/>",
		data:{
			roleIDs:checkedRoles.value().join(","),
			userIDs:userIDs.join(","),
		},
		success:function(resp){
			if (!resp.saved)
				return alert("저장되지 않았습니다.");
			if (currentUsers)
				currentUsers();
			else
				location.reload();
		}
	});
}
</vtx:script>
<vtx:script type="docReady">
	docTitle("ROLE 사용자 정보");
	subTitle("ROLE 정보");
	setRoles({roles:<vtx:json data="${roles}" mapper="${objectMapper}"/>});
	setUsers({
		users:<vtx:json data="${users}" mapper="${objectMapper}"/>
	});
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>