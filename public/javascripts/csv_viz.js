/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

(function() {
	'use strict';

	var fillCsvTable = function(){
		var tbody = $("#csv_table").find("tbody"); 
		var file_content = $("#display").find("textarea").text();
		var file_lines = file_content.split("\n");
		var newTR, newTD, i, j, rows, line;

		if(ignoreHeader == "true"){
			i = 1;
		}	
		else{
			i = 0;
		}

		for(; i < file_lines.length; i++){
			newTR = $(document.createElement('TR'));
			line = file_lines[i];
			rows = line.split(csvSeparator);

			for(j=0; j < rows.length; j++){
				newTD = $(document.createElement('TD'));
				newTD.html(rows[j]);
				newTR.append(newTD);
			}
			tbody.append(newTR);
			console.log("newTR");
		}
	}

	$(function(){
		fillCsvTable();
	});
}());
