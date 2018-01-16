<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<c:set var="type">${!empty param.type ? param.type : 'checkbox'}</c:set>
<table class="infoList">
	<thead>
		<tr><th width="10%">
				<c:if test="${'checkbox' == type}"><input id="_toggleGroups" type="${type}" /></c:if>
				<c:if test="${'radio' == type}">선택</c:if>
			</th>
			<th>아이디</th>
			<th>이름</th>
		</tr>
	</thead>
	<tbody id="_groupList">
		<c:set var="notFound"><tr><td colspan="3" class="notFound">그룹정보를 찾지 못했습니다.</td></c:set>
		<c:set var="groupRow"><tr>
			<td><input name="_groupID" value="{groupID}" type="${type}" /></td>
			<td>{groupID}</td>
			<td>{groupName}</td>
		</tr></c:set>
	</tbody>
</table>
<script type="text/javascript">
var userSelection;

function _searchGroups(start) {
	ajax({
		url:"<c:url value='/action/group/select.do'/>",
		data:{start:start},
		success:_setGroups
	});	
}

function _setGroups(resp) {
	var groups = resp.groups;
	$("#_groupList").populate({
		data:groups,
		tr:function(row){
			return "${vtx:jstring(groupRow)}"
				.replace(/{groupID}/g, row.GRP_ID)
				.replace(/{groupName}/g, row.GRP_NAME);
		},
		ifEmpty:"${vtx:jstring(notFound)}"
	});
	<c:if test="${'checkbox' == type}">
	userSelection = function() {
		var groupIDs = checkbox("input[name='_groupID']").value();
		return !isEmpty(groupIDs) ?
			elementsOf(groups, "GRP_ID", groupIDs) :
			alert("그룹을 선택하십시오.");
	};
	checkbox("#_toggleGroups").onChange(function(checked){
		checkbox("input[type='checkbox'][name='_groupID']").check(checked);
	});
	</c:if>
	<c:if test="${'radio' == type}">
	userSelection = function() {
		var groupID = $("input[name='_groupID']:checked").val();
		return !isEmpty(groupID) ?
			elementsOf(groups, "GRP_ID", groupID)[0] :
			alert("그룹을 선택하십시오.");
	};
	</c:if>
}
$(function(){
	_setGroups({
		groups:<vtx:json data="${groups}" mapper="${objectMapper}"/>
	});
});
</script>