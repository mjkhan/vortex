<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="create">${empty action}</c:set>
<table class="infoForm">
<c:if test="${!create}">
	<tr><th><label for="actionID">아이디</label></th>
		<td><input id="actionID" value="${action.ACT_ID}" type="text" readonly /></td>
	</tr>
</c:if>
	<tr><th><label for="actionName">이름</label></th>
		<td><input id="actionName" value="${action.ACT_NAME}" type="text" required maxlength="32" /></td>
	</tr>
	<tr><th><label for="actionPath">경로</label></th>
		<td><input id="actionPath" value="${action.ACT_PATH}" type="text" required maxlength="32" /></td>
	</tr>
	<tr><th><label for="descrption">설명</label></th>
		<td><textarea id="description" rows="5" style="width:100%; line-height:2em;">${action.DESCRP}</textarea>
		</td>
	</tr>
<c:if test="${!create}">
	<tr><th>수정자</th>
		<td>${action.UPD_ID}</td>
	</tr>
	<tr><th>수정시간</th>
		<td><fmt:formatDate value="${action.UPD_TIME}" pattern="yy-MM-dd HH:mm"/></td>
	</tr>
</c:if>
</table>
<div class="inputArea">
	<button onclick="saveAction();" type="button">저장</button>
	<button onclick="closeAction();" type="button">닫기</button>
</div>
<script type="text/javascript">
function saveAction() {
	if (requiredEmpty()) return;
	
	ajax({
		url:"<c:if test='${create}'><c:url value='/action/create.do'/></c:if><c:if test='${!create}'><c:url value='/action/update.do'/></c:if>",
		data:{
			groupID:$("#groupID").val(),
			id:$("#actionID").val(),
			name:$("#actionName").val(),
			path:$("#actionPath").val(),
			description:$("#description").val()
		},
		success:function(resp) {
			if (resp.saved) {
				afterSave = getActions;
				alert("저장됐습니다.");
				getInfo(resp.actionID || $("#actionID").val());
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}

$(function(){
	$("#actionName").focus();
	$("#actionPath").validate({
		test:function(v){
			return v.match(/^[0-9a-zA-Z/.]+$/);
		}
	});
	$(".infoForm input:not([readonly])").onEnterPress(saveAction);
});
</script>