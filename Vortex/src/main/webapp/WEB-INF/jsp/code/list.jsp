<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div id="searchCodes" style="width:100%;">
	<div class="inputArea">
		<select id="groupID" onchange="getCodes();"><c:forEach items="${groups}" var="group">
			<option value="${group.grp_id}">${group.grp_name}</option></c:forEach>
		 </select>
		 <button onclick="getInfo();" type="button" class="add">추가</button>
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
</div>
<div id="codeDetail" class="hidden" style="padding:1em 0;"></div>
<vtx:script type="decl">
var checkedCodes,
	currentCodes,
	afterSave;

function docTitle(title) {
	document.title = title ? "Vortex - " + title : "Vortex";
}

function getCodes() {
	ajax({
		url:"<c:url value='/code/list.do'/>",
		data:{
			groupID:$("#groupID").val()
		},
		success:function(resp) {
			setCodeList(resp);
		}
	});
}

function removeCodes() {
	if (!confirm("선택한 코드를 삭제하시겠습니까?")) return;

	ajax({
		url:"<c:url value='/code/delete.do'/>",
		data:{
			groupID:$("#groupID").val(),
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

function showDetail(show) {
	if (show != false) {
		$("#searchCodes").hide();
		$("#codeDetail").fadeIn();
	} else {
		if (afterSave) {
			afterSave();
			afterSave = null;
		}
		$("#codeDetail").hide();
		$("#searchCodes").fadeIn();
	}
}

function getInfo(code) {
	ajax({
		url:"<c:url value='/code/info.do'/>",
		data:{groupID:$("#groupID").val(), code:code},
		success:function(resp) {
			$("#codeDetail").html(resp);
			showDetail();
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

	checkedCodes = checkbox("input[type='checkbox'][name='code']")
		.onChange(function(checked){
			if (checked)
				$(".showOnCheck").fadeIn();
			else
				$(".showOnCheck").fadeOut();
		});
	checkbox("#toggleChecks").onChange(function(checked){checkedCodes.check(checked);});
	$(".showOnCheck").fadeOut();
}
</vtx:script>
<vtx:script type="docReady">
	docTitle("공통 코드");
	subTitle("공통 코드");
	setCodeList({
		codes:<vtx:json data="${codes}" mapper="${objectMapper}"/>
	});
	currentCodes = getCodes;
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>