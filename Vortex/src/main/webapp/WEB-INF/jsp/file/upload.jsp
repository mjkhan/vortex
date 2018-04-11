<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<form id="uploadForm" action="<c:url value='/file/upload.do'/>" method="POST" enctype="multipart/form-data">
<table class="infoForm">
	<thead>
		<tr><th style="width:5%"><a onclick="add();">(+)</a></th>
			<th>파일</th>
		</tr>
	</thead>
	<tbody id="uploads"><c:set var="uploadRow">
		<tr id="row{index}">
			<th><a onclick="remove({index});">(-)</a></th>
			<td><input name="upload" type="file"/></td>
		</tr></c:set>
	</tbody>
</table>
<div class="inputArea">
	<button onclick="uploadFile();" type="button">업로드</button>
	<button onclick="showDetail(false);" type="button">닫기</button>
</div>
</form>
<script type="text/javascript">
function add() {
	var uploads = $("#uploads"),
		length = uploads.children("tr").length,
		row = "${vtx:jstring(uploadRow)}".replace(/{index}/g, length);
	if (length < 1)
		uploads.append(row);
	else {
		if ($("#uploads input[type='file']")
			.requiredEmpty(function(){
				alert("업로드할 파일을 선택하십시오.");
			})) return;
		uploads.append(row);
	}
}

function remove(index) {
	$("#row" + index).remove();
}

function uploadFile() {
	if ($("#uploads input[type='file']").requiredEmpty(function(){alert("업로드할 파일을 선택하십시오.");})) return;
	
	var formData = new FormData($("form")[0]);
	json({
		url:"<c:url value='/file/upload.do'/>",
		type:"POST",
		data:formData,
		processData:false,
		contentType:false,
		success:function(resp) {
			if (!resp.saved)
				return alert("저장하지 못했습니다.");
			alert("저장됐습니다.");
			afterSave = getFiles;
		}
	});
}

$(function(){
	$("#uploads").html("${vtx:jstring(uploadRow)}".replace(/{index}/g, "0"));
	$("#file").focus();
});
</script>