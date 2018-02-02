<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
	<div class="inputArea">
		 <button onclick="addActions();" type="button">추가</button>
		 <button id="btnRemove" onclick="removeActions();" type="button" class="showOnCheck">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleChecks" type="checkbox" /></th>
				<th width="30%">경로</th>
				<th width="20%">등록시간</th>
			</tr>
		</thead>
		<tbody id="actionList">
		<c:set var="notFound"><tr><td colspan="3" class="notFound">액션정보를 찾지 못했습니다.</td></c:set>
		<c:set var="actionRow"><tr>
				<td><input name="actionID" value="{actionPath}" type="checkbox" /></td>
				<td>{actionPath}</td>
				<td>{insTime}</td>
			</tr></c:set>
		</tbody>
	</table>
	<div class="paging"></div>
<vtx:script type="decl">
var checkedActions,
	currentActions,
	afterSave;

function getActions(start) {
	ajax({
		url:"<c:url value='/permission/action/list.do'/>"
	   ,data:{
	   		permissionID:currentPermission.PMS_ID
	   	   ,start:start || 0
	   }
	   ,success:setActionList
	});
	currentActions = function(){getActions(start);};
}

function addActions() {
	ajax({
		url:"<c:url value='/action/select.do'/>"
	   ,data:{init:true}
	   ,success:function(resp){
	   		dialog({
	   			title:"액션 선택",
	   			content:resp,
	   			onOK:function(selected) {
	   				ajax({
	   					url:"<c:url value='/permission/action/add.do'/>"
	   				   ,data:{
	   				   		permissionID:currentPermission.PMS_ID,
	   				   		actionPath:selected.join()
	   				    }
	   				   ,success:function(resp) {
	   				   		if (resp.saved)
	   				   			getActions();
	   				   		else
	   				   			alert("저장하지 못했습니다.");
	   				    }
	   				});
	   			}
	   		});
	   }
	});
}

function removeActions() {
	if (!confirm("선택한 액션을 삭제하시겠습니까?")) return;

	ajax({
		url:"<c:url value='/permission/action/delete.do'/>",
		data:{
			permissionID:currentPermission.PMS_ID,
			actionPath:checkedActions.values().join(",")
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

function setActionList(resp) {
	$("#actionList").populate({
		data:resp.actions,
		tr:function(row){
			return "${vtx:jstring(actionRow)}"
				.replace(/{actionPath}/g, row.ACT_PATH)
				.replace(/{insTime}/g, row.INS_TIME);
		},
		ifEmpty:"${vtx:jstring(notFound)}"
	});
	
	var showOnCheck = function(checked){
		if (checked)
			$("#actions .showOnCheck").fadeIn();
		else
			$("#actions .showOnCheck").fadeOut();
	};

	checkedActions = checkbox("input[type='checkbox'][name='actionID']")
		.onChange(showOnCheck);
	checkbox("#actions #toggleChecks")
		.onChange(function(checked){
			checkedActions.check(checked);
			showOnCheck(checked);
		})
		.check(false);
	$("#actions .paging").setPaging({
	    start:resp.actionStart
	   ,fetchSize:resp.fetch
	   ,totalSize:resp.totalActions
	   ,func:"getActions({index})"
	});
}
</vtx:script>
<vtx:script type="docReady">
	setActionList({
		actions:<vtx:json data="${actions}" mapper="${objectMapper}"/>
	   ,totalActions:${totalActions}
	   ,actionStart:${actionStart}
	   ,fetch:${fetch}
	});
	currentActions = getActions;
</vtx:script>