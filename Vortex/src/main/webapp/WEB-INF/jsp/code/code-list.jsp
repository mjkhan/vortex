<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
	<div class="inputArea">
		 <button onclick="getInfo();" type="button">추가</button>
		 <button id="btnRemove" onclick="removeCodes();" type="button" class="showOnCheck">삭제</button>
	</div>
	<table class="infoList">
		<thead>
			<tr><th width="10%"><input id="toggleChecks" type="checkbox" /></th>
				<th width="20%">코드</th>
				<th width="30%">코드값</th>
				<th width="20%">수정시간</th>
			</tr>
		</thead>
		<tbody id="codeList">
		<c:set var="notFound"><tr><td colspan="4" class="notFound">코드를 찾지 못했습니다.</td></c:set>
		<c:set var="codeRow"><tr>
				<td><input name="code" value="{code}" type="checkbox" /></td>
				<td><a onclick="getInfo('{code}')">{code}</a></td>
				<td>{value}</td>
				<td>{updTime}</td>
			</tr></c:set>
		</tbody>
	</table>
	<div class="paging"></div>
<vtx:script type="decl">
var checkedCodes,
	currentCodes,
	getInfo,
	afterSave;

function getCodes(start) {
	ajax({
		url:"<c:url value='/code/list.do'/>"
	   ,data:{
	   		groupID:currentGroup.GRP_ID
	   	   ,start:start
	   }
	   ,success:setCodeList
	});
	currentCodes = function(){getCodes(start);};
}

function getInfo(code) {
	ajax({
		url:"<c:url value='/code/info.do'/>"
	   ,data:{groupID:currentGroup.GRP_ID, code:code}
	   ,success:setDetail
	});
};

function removeCodes() {
	if (!confirm("선택한 코드를 삭제하시겠습니까?")) return;

	ajax({
		url:"<c:url value='/code/delete.do'/>",
		data:{
			groupID:currentGroup.GRP_ID,
			code:checkedCodes.values().join(",")
		},
		success:function(resp) {
			if (resp.saved) {
				currentCodes();
			} else {
				alert("저장하지 못했습니다.");
			}
		}
	});
}

function setCodeList(resp) {
	$("#codeList").populate({
		data:resp.codes,
		tr:function(row){
			return "${vtx:jstring(codeRow)}"
				.replace(/{code}/g, row.CD_ID)
				.replace(/{value}/g, row.CD_VAL)
				.replace(/{updTime}/g, row.UPD_TIME);
		},
		ifEmpty:"${vtx:jstring(notFound)}"
	});
	
	var showOnCheck = function(checked){
		if (checked)
			$("#codes .showOnCheck").fadeIn();
		else
			$("#codes .showOnCheck").fadeOut();
	};

	checkedCodes = checkbox("input[type='checkbox'][name='code']")
		.onChange(showOnCheck);
	checkbox("#codes #toggleChecks")
		.onChange(function(checked){
			checkedCodes.check(checked);
			showOnCheck(checked);
		})
		.check(false);
	$("#codes .paging").setPaging({
	    start:resp.codeStart
	   ,fetchSize:resp.fetch
	   ,totalSize:resp.totalCodes
	   ,func:"getCodes({index})"
	});
}
</vtx:script>
<vtx:script type="docReady">
	setCodeList({
		codes:<vtx:json data="${codes}" mapper="${objectMapper}"/>
	   ,totalCodes:${totalCodes}
	   ,codeStart:${codeStart}
	   ,fetch:${fetch}
	});
	currentCodes = getCodes;
</vtx:script>