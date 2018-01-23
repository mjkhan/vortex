<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
	<div class="inputArea">
		<select id="field">
			<option value="">검색조건</option>
			<option value="GRP_ID">아이디</option>
			<option value="GRP_NAME">이름</option>
		 </select>
		 <input id="value" type="search" placeholder="검색어" style="width:40%;"/>
		 <button onclick="searchGroups();" type="button">찾기</button>
		 <button onclick="getGroupInfo();" type="button" class="add">추가</button>
		 <button onclick="removeGroups();" type="button" class="showOnCheck">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleChecks" type="checkbox" /></th>
				<th width="20%">아이디</th>
				<th width="50%">이름</th>
				<th width="20%">등록시간</th>
			</tr>
		</thead>
		<tbody id="groupList">
		<c:set var="notFound"><tr><td colspan="4" class="notFound">공통코드 그룹을 찾지 못했습니다.</td></c:set>
		<c:set var="groupRow"><tr>
			<td><input name="groupID" value="{groupID}" type="checkbox" /></td>
				<td><a onclick="getGroupInfo('{groupID}')" title="그룹정보 보기">{groupID}</a></td>
				<td><a onclick="getGroupCodes('{groupID}')" title="코드목록 보기">{groupName}</a></td>
				<td>{insTime}</td>
			</tr></c:set>
		</tbody>
	</table>
	<div class="paging"></div>
<vtx:script type="decl">
var getGroup,
	currentGroup,
	checkedGroups,
	currentGroups,
	afterSave;

function searchGroups(start) {
	var field = $("#field").val(),
		value = $("#value").val();
	if (value && !field)
		return alert("검색조건을 선택하십시오.");
	ajax({
		url:"<c:url value='/code/group/list.do'/>"
	   ,data:{
			searchBy:field
		   ,searchTerms:value
		   ,start:start
		}
	   ,success:function(resp) {
	   		setGroupList(resp);
	   		setCodeList(resp);
	   	}
	});
	currentGroups = function(){searchGroups(start);};
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

function getGroupInfo(groupID) {
	ajax({
		url:"<c:url value='/code/group/info.do'/>",
		data:{groupID:groupID},
		success:setDetail
	});
}

function setGroupList(resp) {
	getGroup = function(groupID) {
		var groups = resp.groups;
		if (!groups || groups.length < 1) return {};
		if (!groupID) return groups[0];
		var found = elementsOf(groups, "GRP_ID", groupID);
		return found ? found[0] : {};
	};
	currentGroup = getGroup();
	setGroupName(currentGroup.GRP_NAME);
	
	$("#groupList").populate({
		data:resp.groups
	   ,tr:function(row){
			return "${vtx:jstring(groupRow)}"
				.replace(/{groupID}/g, row.GRP_ID)
				.replace(/{groupName}/g, row.GRP_NAME)
				.replace(/{insTime}/g, row.INS_TIME);
		}
	   ,ifEmpty:"${vtx:jstring(notFound)}"
	});

	$("#codeGroups .paging").setPaging({
	    start:resp.groupStart
	   ,fetchSize:resp.fetch
	   ,totalSize:resp.totalGroups
	   ,func:"searchGroups({index})"
	});
	
	checkedGroups = checkbox("input[type='checkbox'][name='groupID']")
		.onChange(function(checked){
			if (checked)
				$("#codeGroups .showOnCheck").fadeIn();
			else
				$("#codeGroups .showOnCheck").fadeOut();
		});
	checkbox("#toggleChecks")
		.onChange(function(checked){checkedGroups.check(checked);})
		.check(false);
}

function getGroupCodes(groupID) {
	currentGroup = getGroup(groupID);
	setGroupName(currentGroup.GRP_NAME);
	getCodes();
}
</vtx:script>
<vtx:script type="docReady">
	$("#value").onEnterPress(searchGroups);
	setGroupList({
		groups:<vtx:json data="${groups}" mapper="${objectMapper}"/>
	   ,totalGroups:${totalGroups}
	   ,groupStart:${groupStart}
	   ,fetch:${fetch}
	});
	currentGroups = searchGroups;
</vtx:script>