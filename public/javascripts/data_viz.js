/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

(function() {
	'use strict';

	var loadData = function(){
		var file_content = $("#display").find("textarea").text();
		if(ignoreHeader == "true"){
			file_content = file_content.substring(file_content.indexOf("\n") + 1);
		}
		data = d3.dsv(csvDelimiter).parseRows(file_content);
		console.log(data);
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
		loadData();
		console.log($("#page-content-wrapper").width());
	});
}());
var data;
