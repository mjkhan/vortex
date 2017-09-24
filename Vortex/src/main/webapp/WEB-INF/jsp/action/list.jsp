<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div id="searchActions" style="width:100%;">
	<div class="search">
		<select id="groupID" onchange="getActions();"><c:forEach items="${groups}" var="group">
			<option value="${group.grp_id}">${group.grp_name}</option></c:forEach>
		 </select>
		 <button onclick="newAction();" type="button" class="add">추가</button>
		 <button id="btnRemove" onclick="removeActions();" type="button" class="hidden">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleChecks" type="checkbox" /></th>
				<th width="20%">이름</th>
				<th width="30%">경로</th>
				<th width="20%">수정</th>
			</tr>
		</thead>
		<tbody id="actionList">
			<c:set var="notFound"><tr><td colspan="4" style="text-align:center;">액션정보를 찾지 못했습니다.</td></c:set>
			<c:set var="actionRow"><tr>
				<td><input name="actionID" value="{actionID}" type="checkbox" /></td>
				<td><a onclick="getAction('{actionID}')">{actionName}</a></td>
				<td>{actionPath}</td>
				<td>{updTime}</td>
			</tr></c:set>
		</tbody>
	</table>
</div>
<div id="actionDetail" style="padding:1em 0;"></div>
<vtx:script type="decl">
var checkedActions,
	currentActions,
	afterSave;

function getActions() {
	ajax({
		url:"<c:url value='/action/list.do'/>",
		data:{
			groupID:$("#groupID").val()
		},
		success:function(resp) {
			setActionList(resp);
		}
	});
}

function removeActions() {
	if (!confirm("선택한 액션을 삭제하시겠습니까?")) return;

	ajax({
		url:"<c:url value='/action/delete.do'/>",
		data:{
			groupID:$("#groupID").val(),
			actionID:checkedActions.values().join(",")
		},
		success:function(resp) {
			if (resp.saved) {
				currentActions();
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}

function showList(show) {
	if (show == false)
		$("#searchActions").hide();
	else
		$("#searchActions").fadeIn();
}

function closeAction() {
	if (afterSave) {
		afterSave();
		afterSave = null;
	}
	$("#actionDetail").hide();
	showList();
}

function newAction() {
	ajax({
		url:"<c:url value='/action/info.do'/>?" + toQuery({groupID:$("#groupID").val()}),
		success:function(resp) {
			showList(false);
			$("#actionDetail").html(resp).fadeIn();
		}
	});
}

function getAction(actionID) {
	ajax({
		url:"<c:url value='/action/info.do'/>?" + toQuery({groupID:$("#groupID").val(), actionID:actionID}),
		success:function(resp) {
			showList(false);
			$("#actionDetail").html(resp).fadeIn();
		}
	});
}

function setActionList(resp) {
	var list = resp.actions,
		rows = [];
	
	if (list.length < 1) {
		rows.push("${vtx:jstring(notFound)}");
	} else {
		var tag = "${vtx:jstring(actionRow)}";
		for (var i = 0; i < list.length; ++i) {
			var row = list[i];
			rows.push(
				tag.replace(/{actionID}/g, row.ACT_ID)
				   .replace(/{actionName}/g, row.ACT_NAME)
				   .replace(/{actionPath}/g, row.ACT_PATH)
				   .replace(/{updTime}/g, row.UPD_TIME)
			);
		}
	}
	$("#actionList").html(rows.join("\n"));

	checkedActions = checkbox("input[type='checkbox'][name='actionID']")
		.onChange(function(checked){
			if (checked)
				$("#btnRemove").fadeIn();
			else
				$("#btnRemove").fadeOut();
		});
	checkbox("#toggleChecks").echo(checkedActions.target);
	$("#btnRemove").fadeOut();
}
</vtx:script>
<vtx:script type="docReady">
	docTitle("액션 정보");
	subTitle("액션 정보");
	setActionList({
		actions:<vtx:json data="${actions}" mapper="${objectMapper}"/>
	});
	currentActions = getActions;
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>