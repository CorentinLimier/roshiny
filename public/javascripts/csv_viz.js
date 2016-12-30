/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

(function() {
	'use strict';

	$(function(){
		var tbody = $("#csv_table").find("tbody"); 
		var file_content = $("#display").find("textarea").text();
		var file_lines = file_content.split("\n");
		var newTR, newTD, i, j, rows, line;

		for(i=0; i < file_lines.length; i++){
			newTR = $(document.createElement('TR'));
			line = file_lines[i];
			rows = line.split(",");

			for(j=0; j < rows.length; j++){
				newTD = $(document.createElement('TD'));
				newTD.html(rows[j]);
				newTR.append(newTD);
			}
			tbody.append(newTR);
		}
		console.log(file_content)
	});
}());
