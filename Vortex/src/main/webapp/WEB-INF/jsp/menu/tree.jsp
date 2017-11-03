<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<c:set var="css" scope="request">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css" />
</c:set>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div id="tree" style="width:100%;">
${menus}
</div>
<div class="inputArea">
	<button onclick="newMenu();" class="hidden">추가</button>
	<button onclick="getMenu(selectedID());" class="hidden">수정</button>
</div>
<div id="menuDetail" style="padding:1em 0;"></div>
<vtx:script type="src">
<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js" type="text/javascript"></script>
<script src="<c:url value="/asset/js/jstree-helper.js"/>" type="text/javascript"></script>
</vtx:script>
<vtx:script type="decl">
var tree,
	helper,
	afterSave;

function reload() {
	location.reload();
<%-- 	
	ajax({
		url:"<c:url value='/menu/tree.do'/>?reload=true",
		success:function(resp){
			tree = helper = null;
			initTree(resp.menus);
			afterSave = null;
		}
	});
 --%>	
}

function showDetail(show) {
	if (show != false) {
		$("#tree").hide();
		$("#menuDetail").fadeIn();
	} else {
		if (afterSave)
			afterSave();
		$("#menuDetail").hide();
		$("#tree").fadeIn();
	}
}

function selectedID() {
	return helper.selectedNodes()[0];
}

function newMenu() {
	if (!selectedID())
		return alert("메뉴를 추가하려면 부모 메뉴를 선택해야 합니다.");
	ajax({
		url:"<c:url value='/menu/info.do'/>",
		success:function(resp) {
			$("#menuDetail").html(resp);
			showDetail();
		}
	});
}

function getMenu(menuID) {
	if (!menuID || "00000" == menuID) return;
	ajax({
		url:"<c:url value='/menu/info.do'/>?menuID=" + menuID,
		success:function(resp) {
			$("#menuDetail").html(resp);
			showDetail();
		}
	});
}

function initTree(menus) {
	tree = $("#tree").html(menus).jstree({
		plugins:["checkbox", "dnd"]
	   ,core:{check_callback:true <%-- To enable tree modification --%>
		     ,multiple:false
			 }
	   ,checkbox:{whole_node:false  <%-- To separate nodes selected and checked --%>
	   		 ,tie_selection:false
	   		 }
	});
	
	helper = helpTree("#tree", {
		trace:true,
		onNodeSelect: function(nodes) {$(".inputArea .hidden").show();},
		onNodeMove: function(data) {console.log(data.node.id + " MOVED to " + data.parent);},
		onNodeReorder: function(data) {console.log(data.node.id + " REORDERD in " + data.parent + " with offset: " + data.offset + ".");}
	});
	
	helper.open();
}
</vtx:script>
<vtx:script type="docReady">
docTitle("메뉴 정보");
subTitle("메뉴 정보");

initTree("${vtx:jstring(menus)}");
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>