/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

(function() {
	'use strict';

	var addActionButtons = function(tr){
		var newTD = $(document.createElement('TD'));
		var updateButton, deleteButton, addButton;

		updateButton = $(document.createElement('I'))
			.attr("class", "fa fa-pencil-square-o update-row");

		deleteButton = $(document.createElement('I'))
			.attr("class", "fa fa-minus little-margin delete-row");

		addButton = $(document.createElement('I'))
			.attr("class", "fa fa-plus little-margin add-row");


		$(deleteButton).on('click', function(e){deleteRow($(this))});
		$(addButton).on('click', function(e){addRow($(this))});
		$(updateButton).on('click', function(e){updateRow($(this))});

		newTD.append(updateButton);
		newTD.append(deleteButton);
		newTD.append(addButton);
		tr.append(newTD);
	}

	var deleteRow = function(button){
		var tr = button.closest("tr");
		var table = button.closest("table");
		tr.remove();
	}

	var addRow = function(button){
		var trButton = button.closest("tr");
		var newTR = $(document.createElement('TR'));
		var i, newTD, updateButton;

		for(i=0; i<columnTypes.length; i++){
			newTD = $(document.createElement('TD'));
			newTR.append(newTD);
		}

		addActionButtons(newTR);
		if(trButton.parent().prop("tagName") == "THEAD"){
			$("#csv_table tbody").prepend(newTR);
		}
		else{
			trButton.after(newTR);
		}
		updateButton = $(newTR).find(".update-row"); 
		updateRow(updateButton);
	}

	var validateRow = function(button){
		var trButton = button.closest("tr");
		var tds = trButton.find("td");
		var i=0;
		var textValue;

		tds.each(function(){
			if(i == columnTypes.length){
				$(this).remove();
				return;
			}
			textValue = $(this).find("input").val();
			$(this).empty();
			$(this).html(textValue);
			i++;
		});
		addActionButtons(trButton);
	}

	var updateRow = function(button){
		var trButton = button.closest("tr");
		var tds = trButton.find("td");
		var i = 0;
		var classInput = "";
		var textValue, tdContent;

		tds.each(function(){
			if(i == columnTypes.length){
				tdContent = $(document.createElement('I'))
					.attr("class", "fa fa-check validate-row");
				$(tdContent).on('click', function(e){validateRow($(this))});
			}
			else{
				textValue = $(this).html();  
				tdContent = $(document.createElement('INPUT'))
					.attr("type", "text")
					.attr("class", "form-control")
					.attr("value", textValue);
				if(columnTypes[i] == "Date"){
					tdContent.addClass("custom_datepicker");
					$(tdContent).datepicker({
						format: datePickerFormat,
						language: 'fr',
						autoclose: 'true'
					});
				}
			}
			$(this).empty();
			$(this).append(tdContent);
			i++;
		})
	}

	var fillCsvTable = function(){
		var tbody = $("#csv_table").find("tbody"); 
		var file_content = $("#display").find("textarea").text();
		var file_lines = file_content.split("\n");
		var newTR, newTD, i, j, rows, line;

		tbody.empty();

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

			if(usage == "data-in"){
				addActionButtons(newTR);
			} 

			tbody.append(newTR);
		}
	}

	var sendUpdates = function(){
		var contentFile = "";
		var textarea = $("#file_content");
		var lines, i, j;
		
		$(".validate-row").each(function(){
			validateRow($(this));
		});

		if(ignoreHeader == "true"){
			lines = $("#csv_table").find("tr");
			console.log(lines);
		}
		else{
			lines = $("#csv_table tbody").find("tr");
		}

		j = 0;
		lines.each(function(){
			i = 0;
			$(this).find("td, th").each(function(){
				if(i == columnTypes.length){
					return;
				}

				contentFile += $(this).html(); 

				if(i < columnTypes.length - 1){
					contentFile += ";"
				}
				i++;
			})
			
			if(j < lines.length - 1){
				contentFile += "\n";
			}
			j++;
		})

		textarea.empty();
		textarea.html(contentFile);
		$("#submit_updates").trigger("click");
	}

	$(function(){
		$("#li_csv_viz").click(fillCsvTable);
		$("#send_csv_updates").click(sendUpdates);
		$(".add-row").on('click', function(e){addRow($(this))});
		$('.custom_datepicker').datepicker({
		    format: datePickerFormat,
		    language: 'fr',
			autoclose: 'true'
		});
	});
}());
