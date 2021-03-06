<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div style="width:100%;">
	<form id="loginForm" action="<c:url value='/login'/>" method="POST">
<%-- 	
		<input name="${_csrf.parameterName}" value="${_csrf.token}" type="hidden"/>
 --%>
		<div><label for="userID">아이디</label>
			 <input id="userID" name="userID" type="text" required placeholder="로그인 아이디" />
		</div>
		<div><label for="passwd">비밀번호</label>
			 <input id="passwd" name="passwd" type="password" required placeholder="비밀번호" />
		</div>
		<div><input name="remember" type="checkbox" /> 자동 로그인
		</div>
		<div id="msg"></div>
		<div><button onclick="login();" type="button">로그인</button>
		</div>
	</form>
</div>
<vtx:script type="decl">
function login() {
	if (hasEmptyValue()) return;

	json({
		url:"<c:url value='/login'/>",
		data:{
			userID:$("#userID").val(),
			passwd:$("#passwd").val()
		},
		success:function(resp){
			if (resp.authenticated)
				location.href = "<c:url value='/'/>";
			else {
				alert("로그인하지 못했습니다.\n\n올바른 아이디와 비밀번호를 입력하십시오.");
				$("#userID").focus().select();
			}
		}
	});
<%-- 		
	$("#loginForm").submit();
--%>
}
</vtx:script>
<vtx:script type="docReady">
docTitle("로그인");
subTitle("로그인");
if (location.href.indexOf("error") > -1)
	$("#msg").message("로그인하지 못했습니다.");

$("input").onEnterPress(login);
$("#userID").focus();
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>