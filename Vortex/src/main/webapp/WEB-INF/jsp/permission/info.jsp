<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="create">${empty permission}</c:set>
<table class="infoForm">
	<tr><th><label for="permissionID">아이디</label></th>
		<td><input id="permissionID" value="${permission.PMS_ID}" type="text" ${create ? 'required  maxlength="32"' : 'readonly'}/></td>
	</tr>
	<tr><th><label for="permissionName">이름</label></th>
		<td><input id="permissionName" value="${permission.PMS_NAME}" type="text" required maxlength="64" /></td>
	</tr>
	<tr><th><label for="descrption">설명</label></th>
		<td><textarea id="description" rows="5" style="width:100%; line-height:2em;">${permission.DESCRP}</textarea>
		</td>
	</tr>
<c:if test="${!create}">
	<tr><th>수정자</th>
		<td>${permission.UPD_ID}</td>
	</tr>
	<tr><th>수정시간</th>
		<td><fmt:formatDate value="${permission.UPD_TIME}" pattern="yy-MM-dd HH:mm"/></td>
	</tr>
</c:if>
</table>
<div class="inputArea">
	<button onclick="savePermission();" type="button">저장</button>
	<button onclick="showDetail(false);" type="button">닫기</button>
</div>
<script type="text/javascript">
function savePermission() {
	if (hasEmptyValue()) return;
	
	ajax({
		url:"<c:if test='${create}'><c:url value='/permission/create.do'/></c:if><c:if test='${!create}'><c:url value='/permission/update.do'/></c:if>"
	   ,data:{
			id:$("#permissionID").val()
		   ,name:$("#permissionName").val()
		   ,description:$("#description").val()
		}
	   ,success:function(resp) {
			if (resp.saved) {
				<c:if test='${create}'>afterSave = search;</c:if>
				<c:if test='${!create}'>afterSave = currentPermissions;</c:if>
				alert("저장됐습니다.");
				getInfo(resp.permissionID || $("#permissionID").val());
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}
$(function(){
	<c:if test="${create}">
	$("#permissionID").validate({
		test:function(v){
			return v.match(/^[0-9a-zA-Z/.-]+$/);
		}
	}).focus();
	</c:if>
	<c:if test="${!create}">
	$("#permissionName").focus();
	</c:if>
	$(".infoForm input:not([readonly])").onEnterPress(savePermission);
});
</script>