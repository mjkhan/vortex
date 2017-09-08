<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div style="width:100%; padding:2em;">
요청 수행 중 오류가 발생했습니다.
</div>
<vtx:script type="docReady">
	docTitle("오류: 500");
	subTitle("서버 오류");
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>