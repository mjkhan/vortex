<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="create">${empty role}</c:set>
<table class="infoForm">
	<tr><th><label for="roleID">아이디</label></th>
		<td><input id="roleID" value="${role.id}" type="text" required maxlength="32" <c:if test="${!create}">readonly</c:if>/></td>
	</tr>
	<tr><th><label for="roleName">이름</label></th>
		<td><input id="roleName" value="${role.name}" type="text" required maxlength="32" /></td>
	</tr>
	<tr><th><label for="descrption">설명</label></th>
		<td><textarea id="description" rows="5" style="width:100%; line-height:2em;">${role.description}</textarea>
		</td>
	</tr>
<c:if test="${!create}">
	<tr><th>등록</th>
		<td><fmt:formatDate value="${role.createdAt}" pattern="yy-MM-dd hh:mm"/></td>
	</tr>
	<tr><th>수정</th>
		<td><fmt:formatDate value="${role.lastModified}" pattern="yy-MM-dd hh:mm"/></td>
	</tr>
</c:if>
</table>
<div style="padding:.5em 0;">
	<button onclick="saveRole();" type="button">저장</button>
	<button onclick="closeRole();" type="button">닫기</button>
</div>
<script type="text/javascript">
function saveRole() {
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
		url:"<c:if test='${create}'><c:url value='/role/create.do'/></c:if><c:if test='${!create}'><c:url value='/role/update.do'/></c:if>",
		data:{
			roleID:$("#roleID").val(),
			roleName:$("#roleName").val(),
			description:$("#description").val()
		},
		success:function(resp) {
			if (resp.saved) {
				<c:if test='${create}'>afterSave = getRoles;</c:if>
				<c:if test='${!create}'>afterSave = currentRoles;</c:if>
				alert("저장됐습니다.");
				getRole($("#roleID").val());
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}
<c:if test="${create}">$("#roleID").focus();</c:if>
<c:if test="${!create}">$("#roleName").focus();</c:if>
enterPressed(".infoForm input:not([readonly])", saveRole);
</script>