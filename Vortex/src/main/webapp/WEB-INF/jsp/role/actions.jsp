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
				<td><a onclick="getActions('{roleID}')">{roleID}</a></td>
				<td>{roleName}</td>
			</tr></c:set>
		</tbody>
	</table>
</div>
<div id="roleActions" style="padding:1em 0;">
	<div id="actionTitle" class="subTitle">액션 정보</div>
	<div class="inputArea">
		 <button id="btnAdd" onclick="addActions();" type="button" class="add hidden">추가</button>
		 <button id="btnRemove" onclick="deleteActions();" type="button" class="hidden">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleActions" type="checkbox" /></th>
				<th width="30%">이름</th>
				<th width="40%">경로</th>
				<th width="20%">등록시간</th>
			</tr>
		</thead>
		<tbody id="actionList">
		<c:set var="actionNotFound"><tr><td colspan="4" class="notFound">액션정보를 찾지 못했습니다.</td></c:set>
		<c:set var="actionRow"><tr>
			<td><input name="actionID" value="{actionID}" type="checkbox" /></td>
				<td>{actionName}</td>
				<td>{actionPath}</td>
				<td>{insTime}</td>
			</tr></c:set>
		</tbody>
	</table>
</div>
<vtx:script type="decl">
var checkedRoles,
	checkedActions,
	currentActions;

function setActionTitle(roleID) {
	$("#actionTitle").html(roleID + " 액션 정보");
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
				checkbox("#toggleActions").check(false);
			}
		});
	checkbox("#toggleRoles").onChange(function(checked){
		checkedRoles.check(checked);
	});
}

function getActions(roleID) {
	ajax({
		url:"<c:url value='/role/action/list.do'/>",
		data:{roleID:roleID},
		success:setActions
	});
	setActionTitle(roleID);
	currentActions = function(){getActions(roleID);};
}

function setActions(resp) {
	$("#actionList").populate({
		data:resp.actions,
		tr:function(row){
			return "${vtx:jstring(actionRow)}"
				.replace(/{actionID}/g, row.ACT_ID)
				.replace(/{actionName}/g, row.ACT_NAME)
				.replace(/{actionPath}/g, row.ACT_PATH)
				.replace(/{insTime}/g, row.INS_TIME);
		},
		ifEmpty:"${vtx:jstring(actionNotFound)}"
	});

	$("#btnRemove").fadeOut();
	checkedActions = checkbox("input[type='checkbox'][name='actionID']")
		.onChange(function(checked){
			if (checked) {
				if (checkedRoles.isChecked())
					$("#btnRemove").fadeIn();
			} else
				$("#btnRemove").fadeOut();
		});
	checkbox("#toggleActions").onChange(function(checked){checkedActions.check(checked);});
}

function addActions(){
	ajax({
		url:"<c:url value='/action/select.do'/>",
		success:function(resp) {
			dialog.show({
				title:"액션 선택",
				content:resp,
				onOK:function(){
					var actionIDs = actionInfo.checked.value();
					if (!actionIDs)
						return alert("액션을 선택하십시오.");
					
					dialog.close();
					ajax({
						url:"<c:url value='/role/action/add.do'/>",
						data:{
							roleIDs:checkedRoles.value().join(","),
							actionIDs:actionIDs.join(","),
						},
						success:function(resp){
							if (!resp.affected)
								return alert("이미 등록된 액션입니다.");
							if (currentActions)
								currentActions();
							else
								location.reload();
						}
					});
				}
			});
		}
	});
}

function deleteActions(){
	if (!confirm("선택한 액션을 ROLE에서 삭제하시겠습니까?")) return;
	
	var actionIDs = checkedActions.value();
	ajax({
		url:"<c:url value='/role/action/delete.do'/>",
		data:{
			roleIDs:checkedRoles.value().join(","),
			actionIDs:actionIDs.join(","),
		},
		success:function(resp){
			if (!resp.saved)
				return alert("저장되지 않았습니다.");
			if (currentActions)
				currentActions();
			else
				location.reload();
		}
	});
}
</vtx:script>
<vtx:script type="docReady">
	docTitle("ROLE 액션 정보");
	subTitle("ROLE 정보");
	setRoles({roles:<vtx:json data="${roles}" mapper="${objectMapper}"/>});
	setActions({
		actions:<vtx:json data="${actions}" mapper="${objectMapper}"/>
	});
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>