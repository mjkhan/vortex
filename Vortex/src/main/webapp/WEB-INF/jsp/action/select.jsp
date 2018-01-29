<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<c:set var="type">${!empty param.type ? param.type : 'checkbox'}</c:set>
<div class="inputArea">
	<label for="_prefixes">구분</label>
	<select id="_prefixes" onchange="_getActions();"><c:forEach items="${prefixes}" var="prefix">
		<option value="${prefix}">${prefix}</option></c:forEach>
	</select>
</div>
<table class="infoList">
	<thead>
		<tr><th width="10%">
				<c:if test="${'checkbox' == type}"><input id="_toggleActions" type="${type}" /></c:if>
				<c:if test="${'radio' == type}">선택</c:if>
			</th>
			<th>경로</th>
		</tr>
	</thead>
	<tbody id="_actionList">
		<c:set var="notFound"><tr><td colspan="2" class="notFound">액션정보를 찾지 못했습니다.</td></c:set>
		<c:set var="actionRow"><tr>
			<td><input name="_actionPath" value="{actionPath}" type="${type}" /></td>
			<td>{actionPath}</td>
		</tr></c:set>
	</tbody>
</table>
<script type="text/javascript">
var userSelection;

function _getActions(prefix) {
	ajax({
		url:"<c:url value='/action/select.do'/>",
		data:{
			prefix:$("#_prefixes").val()
		},
		success:_setActions
	});	
}

function _setActions(resp) {
	var actions = resp.actions;
	$("#_actionList").populate({
		data:actions,
		tr:function(row){
			return "${vtx:jstring(actionRow)}".replace(/{actionPath}/g, row);
		},
		ifEmpty:"${vtx:jstring(notFound)}"
	});
	<c:if test="${'checkbox' == type}">
	userSelection = function() {
		var actionPaths = checkbox("input[name='_actionPath']").value();
		return !isEmpty(actionPaths) ?
			actionPaths :
			alert("액션을 선택하십시오.");
	};
	checkbox("#_toggleActions").onChange(function(checked){
		checkbox("input[type='checkbox'][name='_actionPath']").check(checked);
	});
	</c:if>
	<c:if test="${'radio' == type}">
	userSelection = function() {
		var actionPaths = $("input[name='_actionPath']:checked").val();
		return !isEmpty(actionPaths) ?
			actionPaths :
			alert("액션을 선택하십시오.");
	};
	</c:if>
}

$(function(){
	_setActions({
		actions:<vtx:json data="${actions}" mapper="${objectMapper}"/>
	});
});
</script>