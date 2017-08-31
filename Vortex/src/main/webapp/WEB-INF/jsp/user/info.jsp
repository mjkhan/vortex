<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="create">${empty user}</c:set>
<table>
	<tr><th><label for="userID">아이디</label></th>
		<td><input id="userID" value="${user.id}" required <c:if test="${!create}">readonly</c:if>/></td>
	</tr>
	<tr><th><label for="userName">이름</label></th>
		<td><input id="userName" value="${user.name}" required/></td>
	</tr>
	<tr><th><label for="alias">별명</label></th>
		<td><input id="alias" value="${user.alias}"/></td>
	</tr>
<c:if test="${!create}">
	<tr><th>등록</th>
		<td><fmt:formatDate value="${user.createdAt}" pattern="yy-MM-dd hh:mm:ss"/></td>
	</tr>
	<tr><th>수정</th>
		<td><fmt:formatDate value="${user.lastModified}" pattern="yy-MM-dd hh:mm:ss"/></td>
	</tr>
	<tr><th>상태</th>
		<td>${user.status}</td>
	</tr>
</c:if>
</table>
<div>
	<button onclick="saveUser();" type="button">저장</button>
	<button onclick="closeUser();" type="button">닫기</button>
</div>
<script type="text/javascript">
function saveUser() {
	var valid = true;
	$("input[required]").each(function(){
		var input = $(this),
			value = input.val();
		if (!value) {
			var label = $("label[for='" + input.attr("id") + "']").html();
			alert(label + "을(를) 입력하십시오.");
			valid = false;
			return false;
		}
	});
	if (!valid) return;
	
	ajax({
		url:"<c:if test='${create}'><c:url value='/user/create.do'/></c:if><c:if test='${!create}'><c:url value='/user/update.do'/></c:if>",
		data:{
			userID:$("#userID").val(),
			userName:$("#userName").val(),
			alias:$("#alias").val()
		},
		success:function(resp) {
			if (resp.saved) {
				alert("저장됐습니다.");
				getUser($("#userID").val());
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}
<c:if test="${create}">$("#userID").focus();</c:if>
<c:if test="${!create}">$("#userName").focus();</c:if>
</script>