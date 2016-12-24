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

	// Wait until dom is ready
	$(function(){
		$("#appname_form").on('submit', function(e){sendForm(e, $(this))});
		$("#pswd_form").on('submit', function(e){sendForm(e, $(this))});
	});

}());
