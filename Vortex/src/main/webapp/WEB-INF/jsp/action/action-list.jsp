<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
	<div class="inputArea">
		 <button onclick="getInfo();" type="button">추가</button>
		 <button onclick="removeActions();" type="button" class="showOnCheck">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleChecks" type="checkbox" /></th>
				<th width="20%">이름</th>
				<th width="30%">경로</th>
				<th width="20%">수정시간</th>
			</tr>
		</thead>
		<tbody id="actionList">
			<c:set var="notFound"><tr><td colspan="4" class="notFound">액션정보를 찾지 못했습니다.</td></c:set>
			<c:set var="actionRow"><tr>
				<td><input name="actionID" value="{actionID}" type="checkbox" /></td>
				<td><a onclick="getInfo('{actionID}')">{actionName}</a></td>
				<td><a href="{actionUrl}">{actionPath}</a></td>
				<td>{updTime}</td>
			</tr></c:set>
		</tbody>
	</table>
<vtx:script type="decl">
var checkedActions,
	currentActions;

function getActions() {
	ajax({
		url:"<c:url value='/action/list.do'/>",
		data:{
			groupID:currentGroup.GRP_ID
		},
		success:setActionList
	});
}

function removeActions() {
	if (!confirm("선택한 액션을 삭제하시겠습니까?")) return;

	ajax({
		url:"<c:url value='/action/delete.do'/>",
		data:{
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

function getInfo(actionID) {
	ajax({
		url:"<c:url value='/action/info.do'/>"
	   ,data:{
	   		groupID:currentGroup.GRP_ID
	   	   ,actionID:actionID
	    }
	   ,success:setDetail
	});
}

function setActionList(resp) {
	$("#actionList").populate({
		data:resp.actions,
		tr:function(row){
			return "${vtx:jstring(actionRow)}"
				.replace(/{actionID}/g, row.ACT_ID)
				.replace(/{actionName}/g, row.ACT_NAME)
				.replace(/{actionPath}/g, row.ACT_PATH)
				.replace(/{actionUrl}/g, wctx.path + row.ACT_PATH)
				.replace(/{updTime}/g, row.UPD_TIME);
		},
		ifEmpty:"${vtx:jstring(notFound)}"
	});

	var showOnCheck = function(checked){
		if (checked)
			$("#actions .showOnCheck").fadeIn();
		else
			$("#actions .showOnCheck").fadeOut();
	};
	
	checkedActions = checkbox("#actions input[type='checkbox'][name='actionID']")
		.onChange(showOnCheck);
	checkbox("#actions #toggleChecks")
		.onChange(function(checked){
			checkedActions.check(checked);
			showOnCheck(checked);
		})
		.check(false);
}
</vtx:script>
<vtx:script type="docReady">
	setActionList({
		actions:<vtx:json data="${actions}" mapper="${objectMapper}"/>
	});
	currentActions = getActions;
</vtx:script>