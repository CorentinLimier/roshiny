(function() {
	'use strict';

	var sendForm = function(e, form){
		var spanResult;
		e.preventDefault();
		$.ajax({
			url: form.attr('action'),
			type: form.attr('method'),
			data: form.serialize(),
			success: function(data) { 
				console.log('sendForm success');
				$("#ajaxResult").remove();
				spanResult = $(document.createElement('SPAN'))
					.attr("id", "ajaxResult")
					.html(data)
				form.find("div:last-child").append(spanResult);
			},
			error: function(xhr, status, errorThrown) {
				console.log('sendForm error');
			},
			complete: function(xhr, status) {
				console.log('sendForm complete');
			}
		});
	};

	// Wait until dom is ready
	$(function(){
		$("#appname_form").on('submit', function(e){sendForm(e, $(this))});
	});

}());
