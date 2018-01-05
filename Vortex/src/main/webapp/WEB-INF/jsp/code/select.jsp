<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<c:set var="type">${!empty param.type ? param.type : 'radio'}</c:set>
<table class="infoList">
	<thead>
		<tr><th width="10%">
				<c:if test="${'radio' == type}">선택</c:if>
				<c:if test="${'checkbox' == type}"><input id="_toggleCodes" type="${type}" /></c:if>
			</th>
			<th width="20%">코드</th>
			<th width="30%">코드값</th>
		</tr>
	</thead>
	<tbody id="_codeList">
	<c:set var="notFound"><tr><td colspan="3" class="notFound">코드를 찾지 못했습니다.</td></c:set>
	<c:set var="codeRow"><tr>
			<td><input name="_code" value="{code}" type="${type}"/></td>
			<td>{code}</td>
			<td>{value}</td>
		</tr></c:set>
	</tbody>
</table>
<div class="paging"></div>
<script type="text/javascript">
var userSelection;

function _getCodes(start) {
	ajax({
		url:"<c:url value='/code/select.do'/>"
	   ,data:{
	   		groupID:"${param.groupID}"
	   	   ,start:start
	   }
	   ,success:_setCodeList
	});
}

function _setCodeList(resp) {
	var codes = resp.codes;
	$("#_codeList").populate({
		data:codes,
		tr:function(row){
			return "${vtx:jstring(codeRow)}"
				.replace(/{code}/g, row.CD_ID)
				.replace(/{value}/g, row.CD_VAL);
		},
		ifEmpty:"${vtx:jstring(notFound)}"
	});

	<c:if test="${'radio' == type}">
	userSelection = function() {
		var codeID = $("input[name='_code']:checked").val();
		return !isEmpty(codeID) ?
			elementsOf(codes, "CD_ID", codeID)[0] :
			alert("항목을 선택하십시오.");
	};
	</c:if>
	<c:if test="${'checkbox' == type}">	
	checkedCodes = checkbox("input[type='checkbox'][name='_code']");
	checkbox("#_toggleCodes").onChange(function(checked){checkedCodes.check(checked);});

	userSelection = function() {
		var codeIDs = checkbox("input[name='_code']").value();
		return !isEmpty(codeIDs) ?
			elementsOf(codes, "CD_ID", codeIDs) :
			alert("항목을 선택하십시오.");
	};
	</c:if>
	$("#codes .paging").setPaging({
	    start:resp.codeStart
	   ,fetchSize:resp.fetch
	   ,totalSize:resp.totalCodes
	   ,func:"_getCodes({index})"
	});
}

$(function(){
	_setCodeList({
		codes:<vtx:json data="${codes}" mapper="${objectMapper}"/>
	   ,totalCodes:${totalCodes}
	   ,codeStart:${codeStart}
	   ,fetch:${fetch}
	});
});
</script>