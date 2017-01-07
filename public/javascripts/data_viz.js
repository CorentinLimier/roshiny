/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

(function() {
	'use strict';
	var data, ndx, page_width;

	var loadData = function(){
		var file_content = $("#display").find("textarea").text();
		if(ignoreHeader == "true"){
			file_content = file_content.substring(file_content.indexOf("\n") + 1);
		}

		data = d3.dsv(csvDelimiter).parseRows(file_content);
		ndx = crossfilter(data);

		data.forEach(function(d) {
			for(var i=0; i < charts.length; i++){
				if(charts[i].absType == "Date"){
					var parseDate = d3.time.format(charts[i].dateFormat).parse;
					charts[i].newAbsColumn = d.push(parseDate(d[charts[i].absColumn])) -1;
				}
				if(charts[i].ordType == "Date"){
					var parseDate = d3.time.format(charts[i].dateFormat).parse;
					charts[i].newOrdColumn = d.push(parseDate(d[charts[i].ordColumn])) -1;
				}
				if(charts[i].absType == "Number"){
					charts[i].newAbsColumn = d.push(parseFloat(d[charts[i].absColumn])) -1;
				}
				if(charts[i].ordType == "Number"){
					charts[i].newOrdColumn = d.push(parseFloat(d[charts[i].ordColumn])) -1;
				}
			}
		}); 
		for(var i=0; i < charts.length; i++){
			if(charts[i].newAbsColumn !== undefined){
				charts[i].absColumn = charts[i].newAbsColumn;
			}
			if(charts[i].newOrdColumn !== undefined){
				charts[i].ordColumn = charts[i].newOrdColumn;
			}
		}
		console.log(data);
	}

	var createDims = function(){
		for(var i=0; i < charts.length; i++){
			charts[i].dim = ndx.dimension(function(d) {return d[charts[i].ordColumn];});
			if(charts[i].ordType == "String"){
				charts[i].group = charts[i].dim.group().reduceSum(function(d) {return 1;}); 
			}
			else{
				charts[i].group = charts[i].dim.group().reduceSum(function(d) {return 1;}); 
				//charts[i].group = charts[i].dim.group().reduceSum(function(d) {return d[charts[i].ordColumn];}); 
			}
		}
	}

	var displayLineChart = function(chart){
		var dcChart = dc.lineChart("#chart-"+chart.id); 
		var dim = ndx.dimension(function(d) {return d[chart.absColumn];});
		var group, min, max, chart_x, brush;

		if(chart.brush == "true"){
			brush = true
		}
		else{
			brush = false
		}

		if(chart.ordType == "String"){
			group = dim.group().reduceSum(function(d) {return 1});
		}
		else{
			group = dim.group().reduceSum(function(d){return d[chart.ordColumn];}); 
		}

		min = dim.bottom(1)[0][chart.absColumn];
		max = dim.top(1)[0][chart.absColumn];

		if(chart.absType == "Date"){
			chart_x = d3.time.scale().domain([min,max]);
		}
		else{
			chart_x = d3.scale.linear().domain([min,max]);
		}

		dcChart	
			.width(chart.width * page_width / 100)
			.height(chart.height)
			.dimension(dim)
			.brushOn(brush)
			.legend(dc.legend().x(80).y(20).itemHeight(13).gap(5))
			.group(group, chart.ordTitle)
			.yAxisLabel(chart.ordTitle)  
			.xAxisLabel(chart.absTitle)  
			.x(chart_x);
	}

	var displayBarChart = function(chart){
		var dcChart = dc.barChart("#chart-"+chart.id); 
		var dim = ndx.dimension(function(d) {return d[chart.absColumn];});
		var group, min, max, chart_x, brush;
		var parseDate = d3.time.format(chart.dateFormat);

		if(chart.ordType == "String"){
			group = dim.group().reduceSum(function(d) {return 1});
		}
		else{
			group = dim.group().reduceSum(function(d){return d[chart.ordColumn];}); 
		}

		dcChart	
			.width(chart.width * page_width / 100)
			.height(chart.height)
			.dimension(dim)
			.legend(dc.legend().x(80).y(20).itemHeight(13).gap(5))
			.group(group, chart.ordTitle)
			.yAxisLabel(chart.ordTitle)  
			.xAxisLabel(chart.absTitle)  
			.xUnits(dc.units.ordinal)
			.x(d3.scale.ordinal().domain(data.map(function(d){return d[chart.absColumn]})));

		if(chart.absType == "Date"){
			dcChart.xAxis().tickFormat(parseDate);
		}
	}

	var displayPieChart = function(chart){
		var dcChart = dc.pieChart("#chart-"+chart.id); 
		var dim = ndx.dimension(function(d) {return d[chart.absColumn];});
		var group;
		var parseDate = d3.time.format(chart.dateFormat);

		if(chart.ordType == "String"){
			group = dim.group().reduceSum(function(d) {return 1});
		}
		else{
			group = dim.group().reduceSum(function(d){return d[chart.ordColumn];}); 
		}

		dcChart	
			.width(chart.width * page_width / 100)
			.height(chart.height)
			.dimension(dim)
			.group(group);

		if(chart.absType == "Date"){
			dcChart.label(function(d){return parseDate(d.key);});
		}
	}

	var displayNumberChart = function(chart){
		var dcChart = dc.numberDisplay("#chart-"+chart.id); 
		var group;
		var dim = ndx.dimension(function(d) {return 1;});
		if(chart.ordType == "String"){
			group = dim.group().reduceSum(function(d) {return 1});
		}
		else{
			group = dim.group().reduceSum(function(d){return d[chart.ordColumn];}); 
		}

		dcChart
			.group(group)
			.formatNumber(d3.format(".g"))
			.html({
				one: chart.legend + ": %number",
				some: chart.legend + ": %number",
				none: chart.legend + ": %number"
				})
	}

	var displayCharts = function(){
		for(var i=0; i < charts.length; i++){
			if(charts[i].typeChart == "Line chart"){
				displayLineChart(charts[i]);
			}
			if(charts[i].typeChart == "Bar chart"){
				displayBarChart(charts[i]);
			}
			if(charts[i].typeChart == "Pie chart"){
				displayPieChart(charts[i]);
			}
			if(charts[i].typeChart == "Number chart"){
				displayNumberChart(charts[i]);
			}
		}
		dc.renderAll(); 
		$("#link_dataviz").trigger("click");
	}

	var printChartModels = function(){
		for(var i=0; i < charts.length; i++){
			console.log(charts[i].id);
			console.log(charts[i].dataFile);
			console.log(charts[i].typeChart);
			console.log(charts[i].legend);
			console.log(charts[i].position);
			console.log(charts[i].height);
			console.log(charts[i].width);
			console.log(charts[i].absColumn);
			console.log(charts[i].absTitle);
			console.log(charts[i].absType);
			console.log(charts[i].ordColumn);
			console.log(charts[i].ordTitle);
			console.log(charts[i].ordType);
			console.log(charts[i].dateFormat);
			console.log(charts[i].brush);
			console.log("");
		}
	}

	$(function(){
		page_width = $("#page-content-wrapper").width() - 20;
		loadData();
		displayCharts();
	});
}());
