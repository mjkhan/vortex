<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<div class="search">
	<select id="actionGroup" onchange="actionInfo.get();"><c:forEach items="${groups}" var="group">
		<option value="${group.grp_id}">${group.grp_name}</option></c:forEach>
	</select>
</div>
<table class="infoList">
	<thead>
		<tr><th width="10%"><input id="toggleChecks" type="checkbox" /></th>
			<th>이름</th>
			<th>경로</th>
		</tr>
	</thead>
	<tbody id="actionResult">
		<c:set var="notFound"><tr><td colspan="3" class="notFound">액션정보를 찾지 못했습니다.</td></c:set>
		<c:set var="actionRow"><tr>
			<td><input name="actionID" value="{actionID}" type="checkbox" /></td>
			<td>{actionName}</td>
			<td>{actionPath}</td>
		</tr></c:set>
	</tbody>
</table>
<div class="search">
	<button onclick="onOK();" type="button">확인</button>
</div>
<script type="text/javascript">
var actionInfo = {
	get:function(){
		ajax({
			url:"<c:url value='/action/list.do'/>",
			data:{
				groupID:$("#actionGroup").val()
			},
			success:actionInfo.set
		});
	},
	set:function(resp){
		$("#actionResult").populate({
			data:resp.actions,
			tr:function(row){
				return "${vtx:jstring(actionRow)}"
					.replace(/{actionID}/g, row.ACT_ID)
					.replace(/{actionName}/g, row.ACT_NAME)
					.replace(/{actionPath}/g, row.ACT_PATH);
			},
			ifEmpty:"${vtx:jstring(notFound)}"
		});
		actionInfo.checked = checkbox("input[type='checkbox'][name='actionID']");
		checkbox("#toggleChecks").onChange(function(checked){
			actionInfo.checked.check(checked);
		});
	}
};

function onOK(){
	var actionIDs = actionInfo.checked.value();
	dialog.close();
	if (actionIDs && window.actionSelected)
		actionSelected(actionIDs);
}

$(function(){
	actionInfo.set({
		actions:<vtx:json data="${actions}" mapper="${objectMapper}"/>
	});
});
</script>