<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="create">${empty menu}</c:set>
<table class="infoForm">
<c:if test="${!create}">
	<tr><th><label for="menuID">아이디</label></th>
		<td><input id="menuID" value="${menu.id}" type="text" readonly /></td>
	</tr>
</c:if>
	<tr><th><label for="menuName">이름</label></th>
		<td><input id="menuName" value="${menu.name}" type="text" required maxlength="32" /></td>
	</tr>
	<tr><th><label for="actionPath">경로</label></th>
		<td><input id="actionPath" value="${menu.actionPath}" type="text" maxlength="32" /></td>
	</tr>
	<tr><th><label for="imgCfg">이미지</label></th>
		<td><input id="imgCfg" value="${menu.imageConfig}" type="text" maxlength="128" /></td>
	</tr>
<c:if test="${!create}">
	<tr><th>수정</th>
		<td><fmt:formatDate value="${menu.lastModified}" pattern="yy-MM-dd hh:mm"/></td>
	</tr>
</c:if>
</table>
<div class="inputArea">
	<button onclick="saveMenu();" type="button">저장</button>
	<button onclick="showDetail(false);" type="button">닫기</button>
</div>
<script type="text/javascript">
function saveMenu() {
	if (requiredEmpty()) return;
	
	ajax({
		url:"<c:if test='${create}'><c:url value='/menu/create.do'/></c:if><c:if test='${!create}'><c:url value='/menu/update.do'/></c:if>",
		data:{
			menuID:$("#menuID").val(),
			parentID:selectedID(),
			menuName:$("#menuName").val(),
			actionPath:$("#actionPath").val(),
			imgCfg:$("#imgCfg").val()
		},
		success:function(resp) {
			if (resp.saved) {
				alert("저장됐습니다.");
				getMenu(resp.menuID || $("#menuID").val());
				afterSave = reload;
			} else {
				onException(resp);
			}
		}
	});
}
$("#menuName").focus();
$(".infoForm input:not([readonly])").onEnterPress(saveMenu);
</script>