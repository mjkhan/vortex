<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<%! private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd hh:mm"); %>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="initial-scale=1.0, width=device-width">
<link href="<c:url value='/asset/image/favicon.ico'/>" rel="shortcut icon" type="image/x-icon" />
<link href="<c:url value='/asset/image/favicon.ico'/>" rel="icon" type="image/x-icon" />
<link href="<c:url value='/asset/css/styles.css'/>" rel="stylesheet" type="text/css" />
<!--[if lt IE 9]><script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script><![endif]-->
<title>Vortex</title>
</head>
<body>
<header>
	<div class="mainTitle">Vortex</div>
	<div class="subTitle">사용자 정보</div>
</header>
<main>
<div id="searchUsers" style="width:100%;">
	<div class="search">
		<select id="field">
			<option value="">검색조건</option>
			<option value="USER_ID">아이디</option>
			<option value="USER_NAME">이름</option>
			<option value="ALIAS">별명</option>
		 </select>
		 <input id="value" type="text" placeholder="검색어" style="width:40%;"/>
		 <button onclick="getUsers(0);" type="button">찾기</button>
		 <button onclick="newUser();" type="button">추가</button>
		 <button id="btnRemove" onclick="removeUsers();" type="button" class="hidden">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleChecks" type="checkbox" /></th>
				<th width="20%">아이디</th>
				<th width="30%">이름</th>
				<th width="20%">별명</th>
				<th width="20%">등록</th>
			</tr>
		</thead>
		<tbody id="userList">
			<c:set var="notFound"><tr><td colspan="5" style="text-align:center;">사용자를 찾지 못했습니다.</td></c:set>
			<c:set var="userRow"><tr>
				<td><input name="userID" value="{userID}" type="checkbox" /></td>
				<td><a onclick="getUser('{userID}')">{userID}</a></td>
				<td>{userName}</td>
				<td>{alias}</td>
				<td>{insTime}</td>
			</tr></c:set>
		</tbody>
	</table>
	<div class="paging">
		<button type="button">더 보기</button>
	</div>
</div>
<div id="userDetail" style="padding:1em 0;"></div>
</main>
<footer>
</footer>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript" src="<c:url value='/asset/js/base.js'/>"></script>
<script type="text/javascript" src="<c:url value='/asset/js/page.js'/>"></script>
<script type="text/javascript">
var wctx = "${pageContext.request.contextPath}",
	checkedUsers,
	currentUsers,
	afterSave;

function docTitle(title) {
	document.title = title ? "Vortex - " + title : "Vortex";
}

function getUsers(start) {
	var field = $("#field").val(),
		value = $("#value").val();
	if (value && !field)
		return alert("검색조건을 선택하십시오.");
	ajax({
		url:"<c:url value='/user/list.do'/>",
		data:{
			field:field,
			value:value,
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

function showList(show) {
	if (show == false)
		$("#searchUsers").hide();
	else
		$("#searchUsers").fadeIn();
}

function closeUser() {
	if (afterSave) {
		afterSave();
		afterSave = null;
	}
	$("#userDetail").hide();
	showList();
}

function newUser() {
	ajax({
		url:"<c:url value='/user/info.do'/>",
		success:function(resp) {
			showList(false);
			$("#userDetail").html(resp).fadeIn();
		}
	});
}

function getUser(userID) {
	ajax({
		url:"<c:url value='/user/info.do'/>?userID=" + userID,
		success:function(resp) {
			showList(false);
			$("#userDetail").html(resp).fadeIn();
		}
	});
}

function setUserList(resp, start) {
	var list = resp.users,
		length = list.length || 0,
		rows = [];
	
	if (length < 1) {
		rows.push("${vtx:jstring(notFound)}");
	} else {
		var tag = "${vtx:jstring(userRow)}";
		for (var i = 0; i < length; ++i) {
			var row = list[i];
			rows.push(
				tag.replace(/{userID}/g, row.USER_ID)
				   .replace(/{userName}/g, row.USER_NAME)
				   .replace(/{alias}/g, row.ALIAS)
				   .replace(/{insTime}/g, row.INS_TIME)
			);
		}
	}
	if (!start) {
		$("#userList").html(rows.join("\n"));
		$("#btnRemove").fadeOut();
	} else
		$("#userList").append(rows.join("\n"));

	if (resp.more) {
		$(".paging button")
			.removeAttr("onclick")
			.attr("onclick", "getUsers(" + resp.next + ")");
		$(".paging").show();
	} else {
		$(".paging").hide();
	}
	
	checkedUsers = checkbox("input[type='checkbox'][name='userID']")
		.onChange(function(checked){
			if (checked)
				$("#btnRemove").fadeIn();
			else
				$("#btnRemove").fadeOut();
		});
	checkbox("#toggleChecks").echo(checkedUsers.target);
}

$(function(){
	docTitle("사용자 정보");
	enterPressed("#value", getUsers);
	setUserList({
		users:<vtx:json data="${users}" dateFormat="<%=dateFormat%>"/>,
		more:${more},
		next:${next}
	}, 0);
	currentUsers = getUsers;
});
</script>
</body>
</html>