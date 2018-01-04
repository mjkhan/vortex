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
	<tr><th><label for="actionPath">액션</label><img onclick="setAction();" alt="액션 선택" title="액션 선택" src="<c:url value='/asset/image/search.png'/>" style="width:15px; height:15px; margin-left:.5em;"></th>
		<td><input id="actionID" value="${menu.actionID}" type="hidden" />
			<input id="actionPath" value="${menu.actionPath}" type="text" readonly/>
		</td>
	</tr>
	<tr><th><label for="imgCfg">이미지</label></th>
		<td><input id="imgCfg" value="${menu.imageConfig}" type="text" maxlength="128" /></td>
	</tr>
<c:if test="${!create}">
	<tr><th>수정시간</th>
		<td><fmt:formatDate value="${menu.lastModified}" pattern="yy-MM-dd HH:mm"/></td>
	</tr>
</c:if>
</table>
<div class="inputArea">
	<button onclick="saveMenu();" type="button">저장</button>
	<button onclick="showDetail(false);" type="button">닫기</button>
</div>
<script type="text/javascript">
function setAction(){
	ajax({
		url:"<c:url value='/action/select.do'/>",
		data:{type:"radio"},
		success:function(resp) {
			popup.show({
				title:"액션 선택",
				content:resp,
				onOK:function(){
					var selected = actionInfo.value();
					if (!selected)
						return alert("액션을 선택하십시오.");
					$("#actionID").val(selected.ACT_ID);
					$("#actionPath").val(selected.ACT_PATH);

					popup.close();
				}
			});
		}
	});
}

function saveMenu() {
	if (requiredEmpty()) return;
	
	ajax({
		url:"<c:if test='${create}'><c:url value='/menu/create.do'/></c:if><c:if test='${!create}'><c:url value='/menu/update.do'/></c:if>",
		data:{
			id:$("#menuID").val(),
			parentID:selectedID(),
			name:$("#menuName").val(),
			actionID:$("#actionID").val(),
			imageConfig:$("#imgCfg").val()
		},
		success:function(resp) {
			if (resp.saved) {
				alert("저장됐습니다.");
				getMenu(resp.menuID || $("#menuID").val());
				afterSave = reload;
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}
$(function(){
	$("#menuName").focus();
	$(".infoForm input:not([readonly])").onEnterPress(saveMenu);
});
</script>