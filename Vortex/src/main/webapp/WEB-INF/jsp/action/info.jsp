<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="create">${empty action}</c:set>
<table class="infoForm">
<c:if test="${!create}">
	<tr><th><label for="actionID">아이디</label></th>
		<td><input id="actionID" value="${action.id}" type="text" readonly /></td>
	</tr>
</c:if>
	<tr><th><label for="actionName">이름</label></th>
		<td><input id="actionName" value="${action.name}" type="text" required maxlength="32" /></td>
	</tr>
	<tr><th><label for="actionPath">경로</label></th>
		<td><input id="actionPath" value="${action.path}" type="text" required maxlength="32" /></td>
	</tr>
	<tr><th><label for="descrption">설명</label></th>
		<td><textarea id="description" rows="5" style="width:100%; line-height:2em;">${action.description}</textarea>
		</td>
	</tr>
<c:if test="${!create}">
	<tr><th>수정</th>
		<td><fmt:formatDate value="${action.lastModified}" pattern="yy-MM-dd hh:mm"/></td>
	</tr>
</c:if>
</table>
<div style="padding:.5em 0;">
	<button onclick="saveAction();" type="button">저장</button>
	<button onclick="closeAction();" type="button">닫기</button>
</div>
<script type="text/javascript">
function saveAction() {
	var valid = true;
	$("input[required]").each(function(){
		var input = $(this),
			value = input.val();
		if (!value) {
			var label = $("label[for='" + input.attr("id") + "']").html();
			alert(label + "을(를) 입력하십시오.");
			return valid = false;
		}
	});
	if (!valid) return;
	
	ajax({
		url:"<c:if test='${create}'><c:url value='/action/create.do'/></c:if><c:if test='${!create}'><c:url value='/action/update.do'/></c:if>",
		data:{
			groupID:$("#groupID").val(),
			actionID:$("#actionID").val(),
			actionName:$("#actionName").val(),
			actionPath:$("#actionPath").val(),
			description:$("#description").val()
		},
		success:function(resp) {
			if (resp.saved) {
				afterSave = getActions;
				alert("저장됐습니다.");
				getAction(resp.actionID);
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}
$("#actionName").focus();
enterPressed(".infoForm input:not([readonly])", saveAction);
</script>