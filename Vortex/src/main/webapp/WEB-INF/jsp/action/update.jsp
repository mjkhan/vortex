<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<table class="infoForm">
	<tr><th><label for="oldPath">이전 경로</label></th>
		<td><input id="oldPath" type="text" required/></td>
	</tr>
	<tr><th><label for="newPath" onclick="setAction();">새 경로</label><img onclick="setAction();" alt="액션 선택" title="액션 선택" src="<c:url value='/asset/image/search.png'/>" style="width:15px; height:15px; margin-left:.5em;"></th>
		<td><input id="newPath" type="text" required readonly/></td>
	</tr>
</table>
<div class="inputArea">
	<button onclick="updateAction();" type="button">저장</button>
</div>
<vtx:script type="decl">
function setAction(){
	ajax({
		url:"<c:url value='/action/select.do'/>",
		data:{type:"radio"},
		success:function(resp) {
			dialog({
				title:"액션 선택",
				content:resp,
				onOK:function(selected){
					$("#newPath").val(selected);
				}
			});
		}
	});
}

function updateAction(){
	if ($(".infoForm [required]").hasEmptyValue()) return;
	
	ajax({
		url:"",
		type:"POST",
		data:{
			oldPath:$("#oldPath").val(),
			newPath:$("#newPath").val()
		},
		success:function(resp){
			if (resp.saved)
				alert("저장됐습니다.");
			else
				alert("저장하지 못했습니다.");
		}
	});
}
</vtx:script>
<vtx:script type="docReady">
	docTitle("액션 정보");
	subTitle("액션 정보");
	$("#oldPath")
		.onEnterPress(updateAction)
		.focus();
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>