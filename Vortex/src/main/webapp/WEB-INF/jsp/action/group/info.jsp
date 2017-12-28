<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="create">${empty group}</c:set>
<table class="infoForm">
<c:if test="${!create}">
	<tr><th><label for="groupID">아이디</label></th>
		<td><input id="groupID" value="${group.GRP_ID}" type="text" required maxlength="3" <c:if test="${!create}">readonly</c:if>/></td>
	</tr>
</c:if>
	<tr><th><label for="groupName">이름</label></th>
		<td><input id="groupName" value="${group.GRP_NAME}" type="text" required maxlength="64" /></td>
	</tr>
	<tr><th><label for="descrption">설명</label></th>
		<td><textarea id="description" rows="5" style="width:100%; line-height:2em;">${group.DESCRP}</textarea>
		</td>
	</tr>
<c:if test="${!create}">
	<tr><th>등록자</th>
		<td>${group.INS_ID}</td>
	</tr>
	<tr><th>등록시간</th>
		<td><fmt:formatDate value="${group.INS_TIME}" pattern="yy-MM-dd HH:mm"/></td>
	</tr>
	<tr><th>수정자</th>
		<td>${group.UPD_ID}</td>
	</tr>
	<tr><th>수정시간</th>
		<td><fmt:formatDate value="${group.UPD_TIME}" pattern="yy-MM-dd HH:mm"/></td>
	</tr>
</c:if>
</table>
<div class="inputArea">
	<button onclick="saveGroup();" type="button">저장</button>
	<button onclick="closeGroup();" type="button">닫기</button>
</div>
<script type="text/javascript">
function saveGroup() {
	if (requiredEmpty()) return;
	
	ajax({
		url:"<c:if test='${create}'><c:url value='/action/group/create.do'/></c:if><c:if test='${!create}'><c:url value='/action/group/update.do'/></c:if>",
		data:{
			id:$("#groupID").val(),
			name:$("#groupName").val(),
			description:$("#description").val()
		},
		success:function(resp) {
			if (resp.saved) {
				<c:if test='${create}'>afterSave = getGroups;</c:if>
				<c:if test='${!create}'>afterSave = currentGroups;</c:if>
				alert("저장됐습니다.");
				getInfo(resp.groupID || $("#groupID").val());
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}
<c:if test="${create}">$("#groupID").focus();</c:if>
<c:if test="${!create}">$("#groupName").focus();</c:if>
$(".infoForm input:not([readonly])").onEnterPress(saveGroup);
</script>