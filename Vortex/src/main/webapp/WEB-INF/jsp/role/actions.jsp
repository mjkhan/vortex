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
				<td><a onclick="getActions({id:'{roleID}', name:'{roleName}'})">{roleID}</a></td>
				<td>{roleName}</td>
			</tr></c:set>
		</tbody>
	</table>
</div>
<div id="roleActions" style="padding:1em 0;">
	<div id="action" class="subTitle">액션 정보</div>
	<div class="search">
		 <button id="btnAdd" onclick="addActions();" type="button">추가</button>
		 <button id="btnRemove" onclick="removeRoles();" type="button" class="hidden">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleActions" type="checkbox" /></th>
				<th width="40%">아이디</th>
				<th width="30%">이름</th>
				<th width="20%">등록</th>
			</tr>
		</thead>
		<tbody id="actionList">
		<c:set var="actionNotFound"><tr><td colspan="4" class="notFound">액션정보를 찾지 못했습니다.</td></c:set>
		<c:set var="actionRow"><tr>
			<td><input name="actionID" value="{actionID}" type="checkbox" /></td>
				<td>{actionID}</td>
				<td>{actionName}</td>
				<td>{insTime}</td>
			</tr></c:set>
		</tbody>
	</table>
</div>
<vtx:script type="decl">
var checkedActions,
	afterSave;

function addActions(){
	ajax({
		url:"<c:url value='/action/search.do'/>",
		success:function(resp) {
			dialog.show({
				content:resp
			});
		}
	});
}

function getRoles(start) {
	var field = $("#field").val(),
		value = $("#value").val();
	if (value && !field)
		return alert("검색조건을 선택하십시오.");
	ajax({
		url:"<c:url value='/role/list.do'/>",
		data:{
			field:field,
			value:value,
			start:start || 0
		},
		success:function(resp) {
			setRoles(resp);
		}
	});
	currentRoles = function(){getRoles(start);};
}

function showList(show) {
	if (show == false)
		$("#searchRoles").hide();
	else
		$("#searchRoles").fadeIn();
}

function closeRole() {
	if (afterSave) {
		afterSave();
		afterSave = null;
	}
	$("#roleDetail").hide();
	showList();
}

function setRoles(resp, start) {
	var append = start > 0;
	$("#roleList").populate({
		data:resp.roles,
		tr:function(row){
			return "${vtx:jstring(roleRow)}"
				.replace(/{roleID}/g, row.ROLE_ID)
				.replace(/{roleName}/g, row.ROLE_NAME);
		},
		ifEmpty:"${vtx:jstring(notFound)}",
		append:append
	});

	$("#btnRemove").fadeOut();
	
	checkbox("#toggleRoles").onChange(function(checked){
		checkbox("input[type='checkbox'][name='roleID']").check(checked);
	});
}

function getActions(role) {
	ajax({
		url:"<c:url value='/role/action/list.do'/>",
		data:{roleID:role.id},
		success:setActions
	});
}

function setActions(resp) {
	$("#actionList").populate({
		data:resp.actions,
		tr:function(row){
			return "${vtx:jstring(actionRow)}"
				.replace(/{actionID}/g, row.ACT_ID)
				.replace(/{actionName}/g, row.ACT_NAME)
				.replace(/{insTime}/g, row.INS_TIME);
		},
		ifEmpty:"${vtx:jstring(actionNotFound)}"
	});
	checkedActions = checkbox("input[type='checkbox'][name='actionID']")
		.onChange(function(checked){
			if (checked)
				$("#btnRemove").fadeIn();
			else
				$("#btnRemove").fadeOut();
		});
	checkbox("#toggleActions").onChange(function(checked){checkedActions.check(checked);});
}
</vtx:script>
<vtx:script type="docReady">
	docTitle("ROLE 액션 정보");
	subTitle("ROLE 정보");
	enterPressed("#value", getRoles);
	setRoles({
		roles:<vtx:json data="${roles}" mapper="${objectMapper}"/>
	}, 0);
	setActions({
		actions:<vtx:json data="${actions}" mapper="${objectMapper}"/>
	});
	currentRoles = getRoles;
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>