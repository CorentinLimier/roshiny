(function() {
	'use strict';

	var sendForm = function(e, form){
		var spanResult;
		$("#ajaxResult").remove();
		e.preventDefault();
		$.ajax({
			url: form.attr('action'),
			type: form.attr('method'),
			data: form.serialize(),
			success: function(data) { 
				console.log('sendForm success');
				spanResult = $(document.createElement('SPAN'))
					.attr("id", "ajaxResult")
					.html(data)
			},
			error: function(xhr, status, errorThrown) {
				console.log('sendForm error');
				spanResult = $(document.createElement('SPAN'))
					.attr("id", "ajaxResult")
					.html(xhr.responseText)
			},
			complete: function(xhr, status) {
				form.find("div:last-child").append(spanResult);
			}
		});
	};

	var deleteRow= function(button){
		var trButton = button.closest("tr");
		trButton.remove();
	}

	var addDataFileRow = function(button){
		var trButton = button.closest("tr");
		var inputs = trButton.find("input");
		var table = button.closest("table");
		var newTR = $(document.createElement('TR'));
		var validate = true;
		var td, deleteButton, hidden_input;

		inputs.each(function(){
			td = $(document.createElement('TD'));
			hidden_input = $(document.createElement('INPUT'))
				.attr("type", "hidden")
				.attr("name", "form_" + $(this).attr("name") + "[]");
			if($(this).attr('type') == "text"){
				td.html($(this).val());
				$(hidden_input).val($(this).val());
				$(this).val("");
			}
			else if($(this).is(":checked")){
				td.html("Activé");
				$(hidden_input).val("true");
				$(this).attr("checked", false);
			}
			else{
				$(hidden_input).val("false");
				td.html("Désactivé");
			}
			td.append(hidden_input);
			newTR.append(td);
		});

		if(!validate){
			return false;
		}

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
		$("#appname_form").on('submit', function(e){sendForm(e, $(this))});
		$("#pswd_form").on('submit', function(e){sendForm(e, $(this))});
		$("#engine_path_form").on('submit', function(e){sendForm(e, $(this))});
		$("#scenarii_path_form").on('submit', function(e){sendForm(e, $(this))});
		$(".add-row").on('click', function(e){addDataFileRow($(this))});
		$(".delete-row").on('click', function(e){deleteRow($(this))});
	});

}());
