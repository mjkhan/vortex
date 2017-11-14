<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div id="searchGroups" style="width:100%;">
	<div class="inputArea">
		<select id="field">
			<option value="">검색조건</option>
			<option value="GRP_ID">아이디</option>
			<option value="GRP_NAME">이름</option>
		 </select>
		 <input id="value" type="search" placeholder="검색어" style="width:40%;"/>
		 <button onclick="getGroups();" type="button">찾기</button>
		 <button onclick="newGroup();" type="button" class="add">추가</button>
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
				<td><a onclick="getGroup('{groupID}')">{groupID}</a></td>
				<td>{groupName}</td>
				<td>{insTime}</td>
			</tr></c:set>
		</tbody>
	</table>
	<div class="paging">
		<button type="button">더 보기</button>
	</div>
</div>
<div id="groupDetail" class="hidden" style="padding:1em 0;"></div>
<vtx:script type="decl">
var checkedGroups,
	currentGroups,
	afterSave;

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

function showDetail(show) {
	if (show != false) {
		$("#searchGroups").hide();
		$("#groupDetail").fadeIn();
	} else {
		if (afterSave) {
			afterSave();
			afterSave = null;
		}
		$("#groupDetail").hide();
		$("#searchGroups").fadeIn();
	}
}

function newGroup() {
	ajax({
		url:"<c:url value='/code/group/info.do'/>",
		success:function(resp) {
			$("#groupDetail").html(resp);
			showDetail();
		}
	});
}

function getGroup(groupID) {
	ajax({
		url:"<c:url value='/code/group/info.do'/>?groupID=" + groupID,
		success:function(resp) {
			$("#groupDetail").html(resp);
			showDetail();
		}
	});
}

function setGroupList(resp, start) {
	var append = start > 0;
	$("#groupList").populate({
		data:resp.groups,
		tr:function(row){
			return "${vtx:jstring(groupRow)}"
				.replace(/{groupID}/g, row.GRP_ID)
				.replace(/{groupName}/g, row.GRP_NAME)
				.replace(/{insTime}/g, row.INS_TIME);
		},
		ifEmpty:"${vtx:jstring(notFound)}",
		append:append
	});

	if (!append)
		$(".showOnCheck").fadeOut();
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
				$(".showOnCheck").fadeIn();
			else
				$(".showOnCheck").fadeOut();
		});
	checkbox("#toggleChecks").onChange(function(checked){checkedGroups.check(checked);});
}
</vtx:script>
<vtx:script type="docReady">
	docTitle("코드그룹 정보");
	subTitle("코드그룹 정보");
	$("#value").onEnterPress(getGroups);
	setGroupList({
		groups:<vtx:json data="${groups}" mapper="${objectMapper}"/>,
		more:${more},
		next:${next}
	}, 0);
	currentGroups = getGroups;
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>