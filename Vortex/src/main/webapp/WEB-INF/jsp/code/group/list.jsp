<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
	<div class="mainTitle">Vortex</div>
	<div class="subTitle">공통코드 그룹 정보</div>
</header>
<main>
<div id="searchGroups" style="width:100%;">
	<div class="search">
		<select id="field">
			<option value="">검색조건</option>
			<option value="GRP_ID">아이디</option>
			<option value="GRP_NAME">이름</option>
		 </select>
		 <input id="value" type="text" placeholder="검색어" style="width:40%;"/>
		 <button onclick="getGroups();" type="button">찾기</button>
		 <button onclick="newGroup();" type="button">추가</button>
		 <button id="btnRemove" onclick="removeGroups();" type="button" class="hidden">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleChecks" type="checkbox" /></th>
				<th width="20%">아이디</th>
				<th width="50%">이름</th>
				<th width="20%">등록</th>
			</tr>
		</thead>
		<tbody id="groupList"><%--		
			<tr><td><input name="groupID" value="아이디" type="checkbox" /></td>
				<td><a onclick="">아이디</a></td>
				<td>이름</td>
				<td>등록</td>
			</tr>--%>
		</tbody>
	</table>
	<div class="paging">
		<button type="button">더 보기</button>
	</div>
</div>
<div id="groupDetail" style="padding:1em 0;"></div>
</main>
<footer>
</footer>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript" src="<c:url value='/asset/js/base.js'/>"></script>
<script type="text/javascript" src="<c:url value='/asset/js/page.js'/>"></script>
<script type="text/javascript">
var wctx = "${pageContext.request.contextPath}",
	checkedGroups,
	currentGroups,
	afterSave;

function setTitle(title) {
	document.title = title ? "Vortex - " + title : "Vortex";
}

function getGroups(start) {
	var field = $("#field").val(),
		value = $("#value").val();
	if (value && !field)
		return alert("검색조건을 선택하십시오.");
	ajax({
		url:"<c:url value='/code/group/list.do'/>",
		data:{
			field:field,
			value:value,
			start:start || 0
		},
		success:function(resp) {
			setGroupList(resp);
		}
	});
	currentGroups = function(){getGroups(start);};
}

function removeGroups() {
	if (!confirm("선택한 그룹을 삭제하시겠습니까?")) return;

	ajax({
		url:"<c:url value='/code/group/delete.do'/>",
		data:{groupID:checkedGroups.values().join(",")},
		success:function(resp) {
			if (resp.saved) {
				currentGroups();
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}

function showList(show) {
	if (show == false)
		$("#searchGroups").hide();
	else
		$("#searchGroups").fadeIn();
}

function closeGroup() {
	if (afterSave) {
		afterSave();
		afterSave = null;
	}
	$("#groupDetail").hide();
	showList();
}

function newGroup() {
	ajax({
		url:"<c:url value='/code/group/info.do'/>",
		success:function(resp) {
			showList(false);
			$("#groupDetail").html(resp).fadeIn();
		}
	});
}

function getGroup(groupID) {
	ajax({
		url:"<c:url value='/code/group/info.do'/>?groupID=" + groupID,
		success:function(resp) {
			showList(false);
			$("#groupDetail").html(resp).fadeIn();
		}
	});
}

function setGroupList(resp, start) {
	var list = resp.groups,
		length = list.length || 0,
		rows = [];
	
	if (length < 1) {
		rows.push("<tr><td colspan=\"4\" style=\"text-align:center;\">공통코드 그룹을 찾지 못했습니다.</td>");
	} else {
		var tag = "<tr>"
		 + "<td><input name=\"groupID\" value=\"{groupID}\" type=\"checkbox\" /></td>"
		 + "<td><a onclick=\"getGroup('{groupID}')\">{groupID}</a></td>"
		 + "<td>{groupName}</td>"
		 + "<td>{insTime}</td>"
		 + "</tr>";
		for (var i = 0; i < length; ++i) {
			var row = list[i];
			rows.push(
				tag.replace(/{groupID}/g, row.GRP_ID)
				   .replace(/{groupName}/g, row.GRP_NAME)
				   .replace(/{insTime}/g, row.INS_TIME)
			);
		}
	}
	if (!start)
		$("#groupList").html(rows.join("\n"));
	else
		$("#groupList").append(rows.join("\n"));

	if (resp.more) {
		$(".paging button")
			.removeAttr("onclick")
			.attr("onclick", "getGroups(" + resp.next + ")");
		$(".paging").show();
	} else {
		$(".paging").hide();
	}
	
	checkedGroups = checkbox("input[type='checkbox'][name='groupID']")
		.onChange(function(checked){
			if (checked)
				$("#btnRemove").fadeIn();
			else
				$("#btnRemove").fadeOut();
		});
	checkbox("#toggleChecks").echo(checkedGroups.target);
}

$(function(){
	setTitle("코드그룹 정보");
	enterPressed("#value", getGroups);
	setGroupList({
		groups:<vtx:json data="${groups}"/>,
		more:${more},
		next:${next}
	}, 0);
	currentGroups = getGroups;
});
</script>
</body>
</html>