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

	var addRow = function(button){
		var trButton = button.closest("tr");
		var table = button.closest("table");
		var trTable = $(document.createElement('TR'));
		var tdTable, input, i, span;

		for(i=0; i<2; i++){
			tdTable = $(document.createElement('TD'));
			input = $(document.createElement('INPUT'))
				.attr("class", "form-control");

			tdTable.append(input);
			trTable.append(tdTable);
		};

		for(i=0; i<2; i++){
			tdTable = $(document.createElement('TD'));
			input = $(document.createElement('INPUT'))
				.attr("type", "checkbox")
				.attr("class", "checkbox-disabled");
			span = $(document.createElement('SPAN'))
				.attr("class", "little-margin")
				.html("Activer");
			tdTable.append(input);
			tdTable.append(span);
			trTable.append(tdTable);
		};

		trButton.before(trTable);
	}

	// Wait until dom is ready
	$(function(){
		$("#appname_form").on('submit', function(e){sendForm(e, $(this))});
		$("#pswd_form").on('submit', function(e){sendForm(e, $(this))});
		$(".add-row").on('click', function(e){addRow($(this))});
	});

}());
