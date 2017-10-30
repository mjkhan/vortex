<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<c:set var="css" scope="request">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css" />
</c:set>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div id="tree" style="width:100%;">
	<ul><li id="root">Root
			<ul><li id="child-1">Child 1</li>
				<li id="child-2">Child 2
					<ul><li id="child-2-1">Child 2-1</li>
					</ul>
				</li>
				<li id="child-3">Child 3
					<ul><li id="child-3-1">Child 3-1</li>
						<li id="child-3-2">Child 3-2
							<ul><li id="child-3-2-1">Child 3-2-1</li>
								<li id="child-3-2-2">Child 3-2-2</li>
							</ul>
						</li>
					</ul>
				</li>
			</ul>
		</li>
	</ul>
</div>
<div id="drag-txt" class="draggable-txt ui-widget-content" style="float:left; border:2px solid red; background-color:cyan;"><span>Hello, world!</span></div>
<div id="drag-txt2" class="draggable-txt ui-widget-content" style="float:left; border:2px solid red; background-color:cyan;"><span>Hello, world, again!</span></div>
<br clear="both"/>
<p><button id="btn" onclick="toggleFolding();">Expand</button>
   <button onclick="console.log(helper.checkedNodes());">Checked</button>
   <button onclick="console.log(JSON.stringify(helper.selectedNodes()));">Selected Node</button>
   <button onclick="addNode();">Add</button>
   <button onclick="editNode();">Edit</button>
   <button onclick="removeNode();">Remove</button>
</p>
<div id="menuDetail" style="padding:1em 0;"></div>
<vtx:script type="src">
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js" type="text/javascript"></script>
<script src="<c:url value="/asset/js/jstree-helper.js"/>" type="text/javascript"></script>
</vtx:script>
<vtx:script type="decl">
function addNode(label) {
	helper.add(function(node){helper.log("A new node ADDED.");});
}

function editNode() {
	helper.edit(function(node){helper.log("A node EDITED.");});
}

function removeNode() {
	helper.remove(function(node){helper.log("A node REMOVED.")});
}

function toggleFolding(expand) {
	var btn = $("#btn");
 	helper.toggleFolding(
 		"Expand" == btn.text(),
 		function(status){btn.text("expanded" == status ? "Collapse" : "Expand");}
 	);
}

var ctxMenus = {
	"addNode":{label:"Add", action:function(obj){addNode();}}
   ,"editNode":{label:"Edit", action:function(obj){editNode();}, separator_after:true}
   ,"removeNode":{label:"Remove", action:function(obj){removeNode();}}
};

var tree = $("#tree").jstree({
	plugins:["checkbox", "contextmenu", "dnd"]
   ,core:{check_callback:true <%-- To enable tree modification --%>
	     ,multiple:false
		 }
   ,checkbox:{whole_node:false, tie_selection:false} <%-- To separate nodes selected and checked --%>
   ,contextmenu:{items:ctxMenus} 
});

var helper = helpTree("#tree", {
	trace:true,
	onNodeSelect: function(nodes) {console.log(nodes + " SELECTED.");},
	onNodeMove: function(data) {console.log(data.node.id + " MOVED to " + data.parent);},
	onNodeReorder: function(data) {console.log(data.node.id + " REORDERD in " + data.parent + " with offset: " + data.offset + ".");}
});
</vtx:script>
<vtx:script type="docReady">
docTitle("메뉴 정보");
subTitle("메뉴 정보");

helper.open("child-3-2");

$(".draggable-txt").draggable({
	revert: true,
	helper: "clone",
	start:function(evt, ui) {
		return helper.dragStart(evt, this, function(data) {
			console.log(data.dragged.id + " dragged and dropped on the node('" + data.target.id + "')");
		});
	}
});

$("#tree").droppable({
	accept:".draggable-txt",
	cursor:"move"
});
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>