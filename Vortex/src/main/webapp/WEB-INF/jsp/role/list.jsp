<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div id="searchRoles" style="width:100%;">
	<div class="search">
		<select id="field">
			<option value="">검색조건</option>
			<option value="ROLE_ID">아이디</option>
			<option value="ROLE_NAME">이름</option>
		 </select>
		 <input id="value" type="search" placeholder="검색어" style="width:40%;"/>
		 <button onclick="getRoles();" type="button">찾기</button>
		 <button onclick="newRole();" type="button" class="add">추가</button>
		 <button id="btnRemove" onclick="removeRoles();" type="button" class="hidden">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleChecks" type="checkbox" /></th>
				<th width="20%">아이디</th>
				<th width="50%">이름</th>
				<th width="20%">수정</th>
			</tr>
		</thead>
		<tbody id="roleList">
		<c:set var="notFound"><tr><td colspan="4" class="notFound">ROLE을 찾지 못했습니다.</td></c:set>
		<c:set var="roleRow"><tr>
			<td><input name="roleID" value="{roleID}" type="checkbox" /></td>
				<td><a onclick="getRole('{roleID}')">{roleID}</a></td>
				<td>{roleName}</td>
				<td>{updTime}</td>
			</tr></c:set>
		</tbody>
	</table>
	<div class="paging">
		<button type="button">더 보기</button>
	</div>
</div>
<div id="roleDetail" style="padding:1em 0;"></div>
<vtx:script type="decl">
var checkedRoles,
	currentRoles,
	afterSave;

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
			setRoleList(resp);
		}
	});
	currentRoles = function(){getRoles(start);};
}

function removeRoles() {
	if (!confirm("선택한 ROLE을 삭제하시겠습니까?")) return;

	ajax({
		url:"<c:url value='/role/delete.do'/>",
		data:{roleID:checkedRoles.values().join(",")},
		success:function(resp) {
			if (resp.saved) {
				currentRoles();
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
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

function newRole() {
	ajax({
		url:"<c:url value='/role/info.do'/>",
		success:function(resp) {
			showList(false);
			$("#roleDetail").html(resp).fadeIn();
		}
	});
}

function getRole(roleID) {
	ajax({
		url:"<c:url value='/role/info.do'/>?roleID=" + roleID,
		success:function(resp) {
			showList(false);
			$("#roleDetail").html(resp).fadeIn();
		}
	});
}

function setRoleList(resp, start) {
	var append = start > 0;
	$("#roleList").populate({
		data:resp.roles,
		tr:function(row){
			return "${vtx:jstring(roleRow)}"
				.replace(/{roleID}/g, row.ROLE_ID)
				.replace(/{roleName}/g, row.ROLE_NAME)
				.replace(/{updTime}/g, row.UPD_TIME);
		},
		ifEmpty:"${vtx:jstring(notFound)}",
		append:append
	});

	if (!append)
		$("#btnRemove").fadeOut();
	if (resp.more) {
		$(".paging button")
			.removeAttr("onclick")
			.attr("onclick", "getRoles(" + resp.next + ")");
		$(".paging").show();
	} else {
		$(".paging").hide();
	}
	
	checkedRoles = checkbox("input[type='checkbox'][name='roleID']")
		.onChange(function(checked){
			if (checked)
				$("#btnRemove").fadeIn();
			else
				$("#btnRemove").fadeOut();
		});
	checkbox("#toggleChecks").echo(checkedRoles.target);
}
</vtx:script>
<vtx:script type="docReady">
	docTitle("ROLE 정보");
	subTitle("ROLE 정보");
	enterPressed("#value", getRoles);
	setRoleList({
		roles:<vtx:json data="${roles}" mapper="${objectMapper}"/>
	}, 0);
	currentRoles = getRoles;
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>