<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<%	String type = request.getParameter("type");
	if (type == null)
		type = "checkbox";
	pageContext.setAttribute("type", type);
%>
<div class="inputArea">
	<label for="_actionGroup">액션 그룹</label>
	<select id="_actionGroup" onchange="actionInfo.get();"><c:forEach items="${groups}" var="group">
		<option value="${group.grp_id}">${group.grp_name}</option></c:forEach>
	</select>
</div>
<table class="infoList">
	<thead>
		<tr><th width="10%"><c:if test="${'checkbox' == type}"><input id="_toggleActions" type="${type}" /></c:if></th>
			<th>이름</th>
			<th>경로</th>
		</tr>
	</thead>
	<tbody id="_actionList">
		<c:set var="notFound"><tr><td colspan="3" class="notFound">액션정보를 찾지 못했습니다.</td></c:set>
		<c:set var="actionRow"><tr>
			<td><input name="_actionID" value="{actionID}" type="${type}" /></td>
			<td>{actionName}</td>
			<td>{actionPath}</td>
		</tr></c:set>
	</tbody>
</table>
<script type="text/javascript">
var actionInfo = {
	get:function() {
		ajax({
			url:"<c:url value='/action/select.do'/>",
			data:{
				groupID:$("#_actionGroup").val()
			},
			success:actionInfo.set
		});
	},
	set:function(resp) {
		var actions = resp.actions;
		$("#_actionList").populate({
			data:actions,
			tr:function(row){
				return "${vtx:jstring(actionRow)}"
					.replace(/{actionID}/g, row.ACT_ID)
					.replace(/{actionName}/g, row.ACT_NAME)
					.replace(/{actionPath}/g, row.ACT_PATH);
			},
			ifEmpty:"${vtx:jstring(notFound)}"
		});
		<c:if test="${'checkbox' == type}">
		actionInfo.value = function() {
			var actionIDs = checkbox("input[name='_actionID']").value();
			return elementsOf(actions, "ACT_ID", actionIDs);
		};
		checkbox("#_toggleActions").onChange(function(checked){
			checkbox("input[type='checkbox'][name='_actionID']").check(checked);
		});
		</c:if>
		<c:if test="${'radio' == type}">
		actionInfo.value = function() {
			var actionID = $("input[name='_actionID']:checked").val();
			return elementsOf(actions, "ACT_ID", actionID)[0];
		};
		</c:if>
		showOK(actions && actions.length);
	}
};

$(function(){
	actionInfo.set({
		actions:<vtx:json data="${actions}" mapper="${objectMapper}"/>
	});
});
</script>