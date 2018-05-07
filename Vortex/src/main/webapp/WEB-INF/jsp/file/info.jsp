<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<table class="infoForm">
	<tr><th><label for="fileID">아이디</label></th>
		<td><input id="fileID" value="${file.FILE_ID}" type="text" readonly/></td>
	</tr>
	<tr><th><label for="fileName">이름</label></th>
		<td><input id="fileName" value="${file.FILE_NAME}" type="text" readonly /></td>
	</tr>
	<tr><th><label for="filePath">경로</label></th>
		<td><input id="filePath" value="${file.FILE_PATH}" type="text" readonly /></td>
	</tr>
	<tr><th><label for="contentType">컨텐트(MIME) 유형</label></th>
		<td><input id="contentType" value="${file.CNT_TYPE}" type="text" readonly /></td>
	</tr>
	<tr><th><label for="size">크기</label></th>
		<td>${file.UNIT_SIZE}
			<input id="size" value="${file.FILE_SIZE}" type="hidden" />
		</td>
	</tr>
	<tr><th><label for="descrp">설명</label></th>
		<td><textarea id="descrp" rows="4" cols="50">${file.DESCRP}</textarea></td>
	</tr>
	<tr><th>등록자</th>
		<td>${file.INS_ID}</td>
	</tr>
	<tr><th>등록시간</th>
		<td><fmt:formatDate value="${file.INS_TIME}" pattern="yy-MM-dd HH:mm"/></td>
	</tr>
	<tr><th>상태</th>
		<td>${file.STATUS}</td>
	</tr>
</table>
<div class="inputArea">
	<button onclick="saveFile();" type="button">저장</button>
	<button onclick="showDetail(false);" type="button">닫기</button>
</div>
<script type="text/javascript">
function saveFile() {
	ajax({
		url:"<c:url value='/file/update.do'/>",
		data:{
			id:$("#fileID").val(),
			description:$("#descrp").val()
		},
		success:function(resp) {
			if (resp.saved) {
				alert("저장됐습니다.");
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}
</script>