<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div id="searchUsers" style="width:100%;">
	<div class="inputArea">
		<select id="field">
			<option value="">검색조건</option>
			<option value="USER_ID">아이디</option>
			<option value="USER_NAME">이름</option>
			<option value="ALIAS">별명</option>
		 </select>
		 <input id="value" type="search" placeholder="검색어" style="width:40%;"/>
		 <button onclick="getUsers(0);" type="button">찾기</button>
		 <button onclick="getInfo();" type="button" class="add">추가</button>
		 <button onclick="removeUsers();" type="button" class="showOnCheck">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleChecks" type="checkbox" /></th>
				<th width="20%">아이디</th>
				<th width="30%">이름</th>
				<th width="20%">별명</th>
				<th width="20%">등록시간</th>
			</tr>
		</thead>
		<tbody id="userList">
			<c:set var="notFound"><tr><td colspan="5" class="notFound">사용자를 찾지 못했습니다.</td></c:set>
			<c:set var="userRow"><tr>
				<td><input name="userID" value="{userID}" type="checkbox" /></td>
				<td><a onclick="getInfo('{userID}')">{userID}</a></td>
				<td>{userName}</td>
				<td>{alias}</td>
				<td>{insTime}</td>
			</tr></c:set>
		</tbody>
	</table>
	<div class="paging"></div>
</div>
<div id="userDetail" class="hidden" style="padding:1em 0;"></div>
<vtx:script type="decl">
var checkedUsers,
	currentUsers,
	afterSave;

function getUsers(start) {
	var field = $("#field").val(),
		value = $("#value").val();
	if (value && !field)
		return alert("검색조건을 선택하십시오.");
	ajax({
		url:"<c:url value='/user/list.do'/>",
		data:{
			searchBy:field,
			searchTerms:value,
			start:start || 0
		},
		success:function(resp) {
			setUserList(resp, start);
		}
	});
	currentUsers = function(){getUsers(start);};
}

function removeUsers() {
	if (!confirm("선택한 사용자를 삭제하시겠습니까?")) return;

	ajax({
		url:"<c:url value='/user/remove.do'/>",
		data:{userID:checkedUsers.values().join(",")},
		success:function(resp) {
			if (resp.saved) {
				currentUsers();
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}

function showDetail(show) {
	if (show != false) {
		$("#searchUsers").hide();
		$("#userDetail").fadeIn();
	} else {
		if (afterSave) {
			afterSave();
			afterSave = null;
		}
		$("#userDetail").hide();
		$("#searchUsers").fadeIn();
	}
}

function getInfo(userID) {
	ajax({
		url:"<c:url value='/user/info.do'/>?userID=" + userID,
		success:function(resp) {
			$("#userDetail").html(resp);
			showDetail();
		}
	});
}

function setUserList(resp) {
	$("#userList").populate({
		data:resp.users,
		tr:function(row){
			return "${vtx:jstring(userRow)}"
				.replace(/{userID}/g, row.USER_ID)
				.replace(/{userName}/g, row.USER_NAME)
				.replace(/{alias}/g, row.ALIAS)
				.replace(/{insTime}/g, row.INS_TIME);
		},
		ifEmpty:"${vtx:jstring(notFound)}"
	});
	
	var showOnCheck = function(checked){
		if (checked)
			$(".showOnCheck").fadeIn();
		else
			$(".showOnCheck").fadeOut();
	};
	
	checkedUsers = checkbox("input[type='checkbox'][name='userID']")
		.onChange(showOnCheck);
	checkbox("#toggleChecks")
		.onChange(function(checked){
			checkedUsers.check(checked);
			showOnCheck(checked);
		})
		.check(false);
	
	$(".paging").setPaging({
	    start:resp.start
	   ,fetchSize:resp.fetchSize
	   ,totalSize:resp.totalSize
	   ,func:"getUsers({index})"
	});
}
</vtx:script>
<vtx:script type="docReady">
	docTitle("사용자 정보");
	subTitle("사용자 정보");
	$("#value").onEnterPress(getUsers);
	setUserList({
		users:<vtx:json data="${users}" mapper="${objectMapper}"/>,
		start:${start},
		fetchSize:${fetchSize},
		totalSize:${totalSize}
	});
	currentUsers = getUsers;
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>