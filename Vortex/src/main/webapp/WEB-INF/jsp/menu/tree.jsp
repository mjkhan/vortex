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
	<button onclick="newMenu();" class="showOnSelect">추가</button>
	<button onclick="getMenu(selectedID());" class="showOnSelect">수정</button>
	<button onclick="remove();" class="showOnCheck">삭제</button>
</div>
<div id="menuDetail" class="hidden" style="padding:1em 0;"></div>
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

function checkedIDs() {
	return helper.checkedNodes().join(",");
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

function remove() {
	var checked = checkedIDs();
	if (!checked || !confirm("선택한 메뉴를 삭제하시겠습니까?")) return;

	ajax({
		url:"<c:url value='/menu/delete.do'/>",
		data:{menuID:checked},
		success:function(resp) {
			if (resp.saved)
				reload();
		}
	});
}

function move(menuID, parentID) {
	ajax({
		url:"<c:url value='/menu/move.do'/>",
		data:{menuID:menuID, parentID:parentID},
		success:function(resp) {
			if (resp.saved)
				reload();
		}
	});
}

function reorder(menuID, parentID, offset) {
	ajax({
		url:"<c:url value='/menu/reorder.do'/>",
		data:{menuID:menuID, parentID:parentID, offset:offset},
		success:function(resp) {
			if (resp.saved)
				reload();
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
		onNodeSelect: function(nodes) {$(".showOnSelect").show();},
		onNodeMove: function(data) {move(data.node.id, data.parent);},
		onNodeReorder: function(data) {reorder(data.node.id, data.parent, data.offset);},
		onNodeCheck: function(data) {
			if (checkedIDs())
				$(".showOnCheck").show();
			else
				$(".showOnCheck").fadeOut();
		}
	});
	
	helper.open("00000");
	
	$(".showOnSelect").hide();
	$(".showOnCheck").hide();
}
</vtx:script>
<vtx:script type="docReady">
docTitle("메뉴 정보");
subTitle("메뉴 정보");

initTree("${vtx:jstring(menus)}");
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>