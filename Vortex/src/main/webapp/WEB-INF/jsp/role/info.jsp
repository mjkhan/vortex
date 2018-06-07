<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="create">${empty role}</c:set>
<input id="roleID" value="${role.GRP_ID}" type="hidden">
<table class="infoForm">
	<tr><th><label for="roleName">이름</label></th>
		<td><input id="roleName" value="${role.GRP_NAME}" type="text" required maxlength="32" /></td>
	</tr>
	<tr><th><label for="descrption">설명</label></th>
		<td><textarea id="description" rows="5" style="width:100%; line-height:2em;">${role.DESCRP}</textarea>
		</td>
	</tr>
<c:if test="${!create}">
	<tr><th>수정자</th>
		<td>${role.UPD_ID}</td>
	</tr>
	<tr><th>수정시간</th>
		<td><fmt:formatDate value="${role.UPD_TIME}" pattern="yy-MM-dd HH:mm"/></td>
	</tr>
</c:if>
</table>
<div class="inputArea">
	<button onclick="saveRole();" type="button">저장</button>
	<button onclick="showDetail(false);" type="button">닫기</button>
</div>
<script type="text/javascript">
function saveRole() {
	if (hasEmptyValue()) return;
	
	ajax({
		url:"<c:if test='${create}'><c:url value='/role/create.do'/></c:if><c:if test='${!create}'><c:url value='/role/update.do'/></c:if>",
		data:{
			id:$("#roleID").val(),
			name:$("#roleName").val(),
			description:$("#description").val()
		},
		success:function(resp) {
			if (resp.saved) {
				<c:if test='${create}'>afterSave = search;</c:if>
				<c:if test='${!create}'>afterSave = currentRoles;</c:if>
				alert("저장됐습니다.");
				getInfo($("#roleID").val() || resp.roleID);
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}
$(function(){
	$("#roleName").focus();
	$(".infoForm input:not([readonly])").onEnterPress(saveRole);
});
</script>