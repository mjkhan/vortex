<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="create">${empty group}</c:set>
<table class="infoForm">
	<tr><th><label for="groupID">아이디</label></th>
		<td><input id="groupID" value="${group.id}" type="text" required maxlength="3" <c:if test="${!create}">readonly</c:if>/></td>
	</tr>
	<tr><th><label for="groupName">이름</label></th>
		<td><input id="groupName" value="${group.name}" type="text" required maxlength="64" /></td>
	</tr>
	<tr><th><label for="descrption">설명</label></th>
		<td><textarea id="description" rows="5" style="width:100%; line-height:2em;">${group.description}</textarea>
		</td>
	</tr>
<c:if test="${!create}">
	<tr><th>등록</th>
		<td><fmt:formatDate value="${group.createdAt}" pattern="yy-MM-dd hh:mm"/></td>
	</tr>
	<tr><th>수정</th>
		<td><fmt:formatDate value="${group.lastModified}" pattern="yy-MM-dd hh:mm"/></td>
	</tr>
</c:if>
</table>
<div style="padding:.5em 0;">
	<button onclick="saveGroup();" type="button">저장</button>
	<button onclick="closeGroup();" type="button">닫기</button>
</div>
<script type="text/javascript">
function saveGroup() {
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
		url:"<c:if test='${create}'><c:url value='/group/create.do'/></c:if><c:if test='${!create}'><c:url value='/group/update.do'/></c:if>",
		data:{
			groupID:$("#groupID").val(),
			groupName:$("#groupName").val(),
			description:$("#description").val()
		},
		success:function(resp) {
			if (resp.saved) {
				<c:if test='${create}'>afterSave = getGroups;</c:if>
				<c:if test='${!create}'>afterSave = currentGroups;</c:if>
				alert("저장됐습니다.");
				getGroup($("#groupID").val());
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}
<c:if test="${create}">$("#groupID").focus();</c:if>
<c:if test="${!create}">$("#groupName").focus();</c:if>
enterPressed(".infoForm input:not([readonly])", saveGroup);
</script>