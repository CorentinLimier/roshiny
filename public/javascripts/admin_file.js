/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

(function() {
	'use strict';

	var deleteRow= function(button){
		var trButton = button.closest("tr");
		trButton.remove();
	}

	// Still ugly - what did you expect
	var addColumnRow = function(button){
		var trButton = button.closest("tr");
		var inputs = trButton.find("input");
		var inputs = trButton.find("input");
		var selects = trButton.find("select");
		var table = button.closest("table");
		var newTR = $(document.createElement('TR'));
		var td, deleteButton, hidden_input, option;

		inputs.each(function(){
			td = $(document.createElement('TD'));
			hidden_input = $(document.createElement('INPUT'))
				.attr("type", "hidden")
				.attr("name", "form_" + $(this).attr("name") + "[]");
			td.html($(this).val());
			$(hidden_input).val($(this).val());
			$(this).val("");
			td.append(hidden_input);
			newTR.append(td);
		});

		selects.each(function(){
			td = $(document.createElement('TD'));
			hidden_input = $(document.createElement('INPUT'))
				.attr("type", "hidden")
				.attr("name", "form_" + $(this).attr("name") + "[]");
			option = $(this).find(":selected").text();
			td.html(option);
			$(hidden_input).val(option);
			td.append(hidden_input);
			newTR.append(td);
		});

		td = $(document.createElement('TD'))
		deleteButton = $(document.createElement('BUTTON'))
			.attr("class", "btn btn-default")
			.html("Supprimer");
		$(deleteButton).on("click", function(e){deleteRow($(this))});
		td.append(deleteButton);
		newTR.append(td);

		trButton.before(newTR);
	}

	// Wait until dom is ready
	$(function(){
		$(".add-row").on('click', function(e){addColumnRow($(this))});
		$(".delete-row").on('click', function(e){deleteRow($(this))});
	});
}());
