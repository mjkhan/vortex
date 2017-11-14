<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="create">${empty code}</c:set>
<table class="infoForm">
	<tr><th><label for="code">코드</label></th>
		<td><input id="code" value="${code.code}" type="text" required maxlength="32" <c:if test="${!create}">readonly</c:if>/></td>
	</tr>
	<tr><th><label for="value">코드값</label></th>
		<td><input id="value" value="${code.value}" type="text" required maxlength="64" /></td>
	</tr>
	<tr><th><label for="descrption">설명</label></th>
		<td><textarea id="description" rows="5" style="width:100%; line-height:2em;">${code.description}</textarea>
		</td>
	</tr>
<c:if test="${!create}">
	<tr><th>수정시간</th>
		<td><fmt:formatDate value="${code.lastModified}" pattern="yy-MM-dd hh:mm"/></td>
	</tr>
</c:if>
</table>
<div class="inputArea">
	<button onclick="saveCode();" type="button">저장</button>
	<button onclick="showDetail(false);" type="button">닫기</button>
</div>
<script type="text/javascript">
function saveCode() {
	if (requiredEmpty()) return;
	
	ajax({
		url:"<c:if test='${create}'><c:url value='/code/create.do'/></c:if><c:if test='${!create}'><c:url value='/code/update.do'/></c:if>",
		data:{
			groupID:$("#groupID").val(),
			code:$("#code").val(),
			value:$("#value").val(),
			description:$("#description").val()
		},
		success:function(resp) {
			if (resp.saved) {
				afterSave = getCodes;
				alert("저장됐습니다.");
				getCode($("#code").val());
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}
<c:if test="${create}">$("#code").focus();</c:if>
<c:if test="${!create}">$("#value").focus();</c:if>
$(".infoForm input:not([readonly])").onEnterPress(saveCode);
</script>