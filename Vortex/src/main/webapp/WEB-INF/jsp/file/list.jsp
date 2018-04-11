<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div id="searchFiles" style="width:100%;">
	<div class="inputArea">
		<select id="field" onchange="$('#value').focus().select();">
			<option value="">검색조건</option>
			<option value="FILE_NAME">이름</option>
			<option value="CNT_TYPE">유형</option>
		 </select>
		 <input id="value" type="search" placeholder="검색어" style="width:40%;"/>
		 <button onclick="getFiles(0);" type="button">찾기</button>
		 <button onclick="newFile();" type="button" class="add">추가</button>
		 <button onclick="removeFiles();" type="button" class="showOnCheck">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleChecks" type="checkbox" /></th>
				<th width="20%">아이디</th>
				<th width="30%">이름</th>
				<th width="20%">컨텐트(MIME) 유형</th>
				<th width="20%">등록시간</th>
			</tr>
		</thead>
		<tbody id="fileList">
			<c:set var="notFound"><tr><td colspan="5" class="notFound">파일을 찾지 못했습니다.</td></c:set>
			<c:set var="fileRow"><tr>
				<td><input name="fileID" value="{fileID}" type="checkbox" /></td>
				<td><a onclick="getInfo('{fileID}')">{fileID}</a></td>
				<td><a href="{url}" target="_blank">{fileName}</a></td>
				<td>{contentType}</td>
				<td>{insTime}</td>
			</tr></c:set>
		</tbody>
	</table>
	<div class="paging"></div>
</div>
<div id="fileDetail" class="hidden" style="padding:1em 0;"></div>
<vtx:script type="decl">
var checkedFiles,
	currentFiles,
	afterSave;

function getFiles(start) {
	var field = $("#field").val(),
		value = $("#value").val();
	if (value && !field)
		return alert("검색조건을 선택하십시오.");
	json({
		url:"<c:url value='/file/list.do'/>",
		data:{
			searchBy:field,
			searchTerms:value,
			start:start || 0
		},
		success:function(resp) {
			setFileList(resp, start);
		}
	});
	currentFiles = function(){getFiles(start);};
}

function newFile() {
	ajax({
		url:"<c:url value='/file/upload.do'/>",
		success:function(resp){
			$("#fileDetail").html(resp);
			showDetail();
		}
	});
}

function removeFiles() {
	if (!confirm("선택한 파일을 삭제하시겠습니까?")) return;

	ajax({
		url:"<c:url value='/file/remove.do'/>",
		data:{fileID:checkedFiles.values().join(",")},
		success:function(resp) {
			if (resp.saved) {
				currentFiles();
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}

function showDetail(show) {
	if (show != false) {
		$("#searchFiles").hide();
		$("#fileDetail").fadeIn();
	} else {
		if (afterSave) {
			afterSave();
			afterSave = null;
		}
		$("#fileDetail").hide();
		$("#searchFiles").fadeIn();
	}
}

function getInfo(fileID) {
	ajax({
		url:"<c:url value='/file/info.do'/>?fileID=" + fileID,
		success:function(resp) {
			$("#fileDetail").html(resp);
			showDetail();
		}
	});
}

function setFileList(resp) {
	$("#fileList").populate({
		data:resp.files,
		tr:function(row){
			return "${vtx:jstring(fileRow)}"
				.replace(/{fileID}/g, row.FILE_ID)
				.replace(/{fileName}/g, row.FILE_NAME)
				.replace(/{url}/g, row.URL)
				.replace(/{contentType}/g, row.CNT_TYPE)
				.replace(/{insTime}/g, row.INS_TIME);
		},
		ifEmpty:"${vtx:jstring(notFound)}"
	});
	
	var showOnCheck = function(checked){
		if (checked)
			$(".showOnCheck").fadeIn();
		else
			$(".showOnCheck").fadeOut();
	};
	
	checkedFiles = checkbox("input[type='checkbox'][name='fileID']")
		.onChange(showOnCheck);
	checkbox("#toggleChecks")
		.onChange(function(checked){
			checkedFiles.check(checked);
			showOnCheck(checked);
		})
		.check(false);
	
	$(".paging").setPaging({
	    start:resp.start
	   ,fetchSize:resp.fetchSize
	   ,totalSize:resp.totalSize
	   ,func:"getFiles({index})"
	});
}
</vtx:script>
<vtx:script type="docReady">
	docTitle("파일 정보");
	subTitle("파일 정보");
	$("#value").onEnterPress(getFiles);
	setFileList({
		files:<vtx:json data="${files}" mapper="${objectMapper}"/>,
		start:${start},
		fetchSize:${fetch},
		totalSize:${totalSize}
	});
	currentFiles = getFiles;
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>