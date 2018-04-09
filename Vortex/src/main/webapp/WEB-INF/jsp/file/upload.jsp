<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<form id="uploadForm" action="<c:url value='/file/upload.do'/>" method="POST" enctype="multipart/form-data">
<table class="infoForm">
	<tr><th><label for="file">파일 선택</label></th>
		<td><input id="file" name="upload" type="file"/></td>
	</tr>
</table>
<div class="inputArea">
	<button onclick="uploadFile();" type="button">업로드</button>
	<button onclick="showDetail(false);" type="button">닫기</button>
</div>
</form>
<script type="text/javascript">
function uploadFile() {
	if ($("#uploadForm input[type='file']").requiredEmpty(function(){alert("업로드할 파일을 선택하십시오.");})) return;
	
	var formData = new FormData($("form")[0]);
	json({
		url:"<c:url value='/file/upload.do'/>",
		type:"POST",
		data:formData,
		processData:false,
		contentType:false,
		success:function(resp) {
			alert(JSON.stringify(resp));
		}
	});
}

$(function(){
	$("#file").focus();
});
</script>