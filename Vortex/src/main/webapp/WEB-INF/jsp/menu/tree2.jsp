<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css" />
<style>
button {cursor:pointer;}
</style>
<title>Tree Test</title>
</head>
<body>
<div id="tree" style="width:400px; height:250px; overflow:auto; float:left;">
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

<script src="https://code.jquery.com/jquery-1.12.4.js" type="text/javascript"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js" type="text/javascript"></script>
<script src="<c:url value="/js/jstree-helper2.js"/>" type="text/javascript"></script>
<script type="text/javascript">
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
</script>
</body>
</html>