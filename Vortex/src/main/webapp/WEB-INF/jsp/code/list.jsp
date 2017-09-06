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
	<div class="subTitle">공통 코드</div>
</header>
<main>
<div id="searchCodes" style="width:100%;">
	<div class="search">
		<select id="groupID" onchange="getCodes();"><c:forEach items="${groups}" var="group">
			<option value="${group.grp_id}">${group.grp_name}</option></c:forEach>
		 </select>
		 <button onclick="newCode();" type="button">추가</button>
		 <button id="btnRemove" onclick="removeCodes();" type="button" class="hidden">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleChecks" type="checkbox" /></th>
				<th width="20%">코드</th>
				<th width="30%">코드값</th>
				<th width="20%">수정</th>
			</tr>
		</thead>
		<tbody id="codeList">
		<c:set var="notFound"><tr><td colspan="4" style="text-align:center;">코드를 찾지 못했습니다.</td></c:set>
		<c:set var="codeRow"><tr>
				<td><input name="code" value="{code}" type="checkbox" /></td>
				<td><a onclick="getCode('{code}')">{code}</a></td>
				<td>{value}</td>
				<td>{updTime}</td>
			</tr></c:set>
		</tbody>
	</table>
</div>
<div id="codeDetail" style="padding:1em 0;"></div>
</main>
<footer>
</footer>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript" src="<c:url value='/asset/js/base.js'/>"></script>
<script type="text/javascript" src="<c:url value='/asset/js/page.js'/>"></script>
<script type="text/javascript">
var wctx = "${pageContext.request.contextPath}",
	checkedCodes,
	currentCodes,
	afterSave;

function docTitle(title) {
	document.title = title ? "Vortex - " + title : "Vortex";
}

function getCodes() {
	ajax({
		url:"<c:url value='/code/list.do'/>",
		data:{
			groupID:$("#groupID").val()
		},
		success:function(resp) {
			setCodeList(resp);
		}
	});
}

function removeCodes() {
	if (!confirm("선택한 코드를 삭제하시겠습니까?")) return;

	ajax({
		url:"<c:url value='/code/delete.do'/>",
		data:{
			groupID:$("#groupID").val(),
			code:checkedCodes.values().join(",")
		},
		success:function(resp) {
			if (resp.saved) {
				currentCodes();
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}

function showList(show) {
	if (show == false)
		$("#searchCodes").hide();
	else
		$("#searchCodes").fadeIn();
}

function closeCode() {
	if (afterSave) {
		afterSave();
		afterSave = null;
	}
	$("#codeDetail").hide();
	showList();
}

function newCode() {
	ajax({
		url:"<c:url value='/code/info.do'/>?" + toQuery({groupID:$("#groupID").val()}),
		success:function(resp) {
			showList(false);
			$("#codeDetail").html(resp).fadeIn();
		}
	});
}

function getCode(code) {
	ajax({
		url:"<c:url value='/code/info.do'/>?" + toQuery({groupID:$("#groupID").val(), code:code}),
		success:function(resp) {
			showList(false);
			$("#codeDetail").html(resp).fadeIn();
		}
	});
}

function setCodeList(resp) {
	var list = resp.codes,
		rows = [];
	
	if (list.length < 1) {
		rows.push("${vtx:jstring(notFound)}");
	} else {
		var tag = "${vtx:jstring(codeRow)}";
		for (var i = 0; i < list.length; ++i) {
			var row = list[i];
			rows.push(
				tag.replace(/{code}/g, row.CD_ID)
				   .replace(/{value}/g, row.CD_VAL)
				   .replace(/{updTime}/g, row.UPD_TIME)
			);
		}
	}
	$("#codeList").html(rows.join("\n"));

	checkedCodes = checkbox("input[type='checkbox'][name='code']")
		.onChange(function(checked){
			if (checked)
				$("#btnRemove").fadeIn();
			else
				$("#btnRemove").fadeOut();
		});
	checkbox("#toggleChecks").echo(checkedCodes.target);
	$("#btnRemove").fadeOut();
}

$(function(){
	docTitle("공통 코드");
	setCodeList({
		codes:<vtx:json data="${codes}" dateFormat="<%=dateFormat%>"/>
	});
	currentCodes = getCodes;
});
</script>
</body>
</html>