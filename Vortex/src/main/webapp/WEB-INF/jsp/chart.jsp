<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="vtx" uri="vortex.tld"%>
<jsp:include page="/WEB-INF/jsp/common/header.jsp"/>
<div id="searchUsers" style="width:100%;">
	<div class="inputArea" style="width:100%; height:auto;">
		<canvas id="mychart"></canvas>
	</div>
	<div class="paging">
		<input id="start" type="number" min="0" placeholder="시작회차"/>
		<button onclick="update($('#start').val());" type="button">update</button>
	</div>
</div>
<vtx:script type="src"><script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.min.js" type="text/javascript"></script></vtx:script>
<vtx:script type="decl">
var result = {
	current:-1,
	data:[
		{label:"1회차", picks:[1, 2, 3, 4, 5, 6]}, 
		{label:"2회차", picks:[2, 2, 3, 4, 5, 6]}, 
		{label:"3회차", picks:[3, 2, 3, 4, 5, 6]}, 
		{label:"4회차", picks:[4, 2, 3, 4, 5, 6]}, 
		{label:"5회차", picks:[5, 2, 3, 4, 5, 6]}, 
		{label:"6회차", picks:[6, 2, 3, 4, 5, 6]}, 
		{label:"7회차", picks:[7, 2, 3, 4, 5, 6]}, 
		{label:"8회차", picks:[8, 2, 3, 4, 5, 6]}, 
		{label:"9회차", picks:[9, 2, 3, 4, 5, 6]}, 
		{label:"10회차", picks:[10, 2, 3, 4, 5, 6]}
	],
	reset:function(start) {
		result.current = (start || 0) - 1;
	},
	next:function() {
		return ++result.current < result.data.length ?
			result.data[result.current] :
			null;
	}
};
var labels = [];
for (var i = 1; i <= 45; ++i)
	labels.push(i);
var cfg = {
		type:"bar",
		data:{
			labels:labels,
			datasets:[{
<%--			label:"Chart test",
				data:[12, 19, 3, 5, 2, 3],
--%>			backgroundColor:[
					"rgba(255, 99, 132, 0.2)",
					"rgba(54, 162, 235, 0.2)",
					"rgba(255, 206, 86, 0.2)",
					"rgba(75, 192, 192, 0.2)",
					"rgba(153, 102, 255, 0.2)",
					"rgba(255, 159, 64, 0.2)"
				],
				borderColor:[
					"rgba(255,99,132,1)",
					"rgba(54, 162, 235, 1)",
					"rgba(255, 206, 86, 1)",
					"rgba(75, 192, 192, 1)",
					"rgba(153, 102, 255, 1)",
					"rgba(255, 159, 64, 1)"
				],
				borderWidth:1
			}]
		},
		options:{
			scales:{
				yAxes:[{
					ticks:{
						beginAtZero:true,
						stepSize:10,
						max:150
					}
				}]
			},
			animation:false,
			onClick:function(evt) {
				log("Chart clicked.");
			}
		}
	},
	chart;

function update(start) {
	result.reset(start);
	var plot = setInterval(function(){
		var next = result.next();
		if (!next) 
			return clearInterval(plot);
		
		chart.data.datasets.forEach(dataset => {
			dataset.label = next.label;
			dataset.data = next.picks;
		});
		chart.update();
	}, 500);
}
</vtx:script>
<vtx:script type="docReady">
	docTitle("Chart test");
	subTitle("Chart test");
	var ctx = $("#mychart");
	chart = new Chart(ctx, cfg);
	update();
	$("#start").onEnterPress(function(){update($("#start").val());});
</vtx:script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>