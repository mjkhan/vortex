<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false" isErrorPage="true"%>
<%@ page import="vortex.support.Assert"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<% Throwable cause = Assert.rootCause(exception);%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div style="width:100%; padding:2em;">
	<p>요청 수행 중 다음과 같은 오류가 발생했습니다.</p>
	<p><%=cause.getClass().getName()%></p>
	<p><%=cause.getMessage()%></p>
</div>
<vtx:script type="docReady">
	docTitle("서버 오류");
	subTitle("서버 오류");
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>