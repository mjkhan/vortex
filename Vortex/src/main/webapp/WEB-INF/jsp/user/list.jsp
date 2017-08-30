<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
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
	<h1>사용자 정보</h1>
</header>
<main>
<div id="searchUsers">
	<div class="search">
		<select id="field">
			<option value="">검색조건</option>
			<option value="USER_ID">아이디</option>
			<option value="USER_NAME">이름</option>
			<option value="ALIAS">별명</option>
		 </select>
		 <input id="value" type="text" placeholder="검색어"/>
		 <button onclick="getUsers(0);" type="button">찾기</button>
		 <button onclick="newUser();" type="button">추가</button>
		 <button type="button" class="hidden">삭제</button>
	</div>
	<table>
		<thead>
			<tr><th><input id="toggleChecks" type="checkbox" /></th>
				<th>아이디</th>
				<th>이름</th>
				<th>별명</th>
				<th>등록</th>
			</tr>
		</thead>
		<tbody id="userList"><%--		
			<tr><td><input name="userID" value="아이디" type="checkbox" /></td>
				<td><a onclick="">아이디</a></td>
				<td>이름</td>
				<td>별명</td>
				<td>등록</td>
			</tr>--%>
		</tbody>
	</table>
	<div class="paging">
		<button type="button">더 보기</button>
	</div>
</div>
<div id="userDetail"></div>
</main>
<footer>
</footer>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript" src="<c:url value='/asset/js/base.js'/>"></script>
<script type="text/javascript" src="<c:url value='/asset/js/page.js'/>"></script>
<script type="text/javascript">
var wctx = "${pageContext.request.contextPath}";

function setTitle(title) {
	document.title = "Vortex";
	if (title)
		document.title = "Vortex - " + title;
}

function getUsers(start) {
	ajax({
		url:"<c:url value='/user/list.do'/>",
		data:{
			field:$("#field").val(),
			value:$("#value").val(),
			start:start
		},
		success:function(resp) {
			setUserList(resp, start);
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
		rows = [];
	
	if (list.length < 1) {
		rows.push("<tr><td colspan=\"5\">사용자를 찾지 못했습니다.</td>");
	} else {
		var tag = "<tr>"
		 + "<td><input name=\"userID\" value=\"{userID}\" type=\"checkbox\" /></td>"
		 + "<td><a onclick=\"getUser('{userID}')\">{userID}</a></td>"
		 + "<td>{userName}</td>"
		 + "<td>{alias}</td>"
		 + "<td>{insTime}</td>"
		 + "</tr>";
		for (var i = 0; i < list.length; ++i) {
			var row = list[i];
			rows.push(
				tag.replace(/{userID}/g, row.USER_ID)
				   .replace(/{userName}/g, row.USER_NAME)
				   .replace(/{alias}/g, row.ALIAS)
				   .replace(/{insTime}/g, row.INS_TIME)
			);
		}
	}
	if (start < 1)
		$("#userList").html(rows.join("\n"));
	else
		$("#userList").append(rows.join("\n"));
	
	if (resp.more) {
		$(".paging button").removeAttr("onclick").attr("onclick", "getUsers(" + resp.next + ")");
		$(".paging").show();
	} else {
		$(".paging").hide();
	}
}

$(function(){
	setTitle("사용자 정보");
	setUserList({
		users:<vtx:json data="${users}"/>,
		more:${more},
		next:${next}
	}, 0);
});
</script>
</body>
</html>