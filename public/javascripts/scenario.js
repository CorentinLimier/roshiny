/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

(function() {
	'use strict';

	var changeOpacity = function(){
		$('body').css('opacity', '0.5');
	}

	var launchScenario = function(e, form){
		e.preventDefault();

		$("#status").html('En cours');
		$("#ajax-load").show();

		$.ajax({
			url: form.attr('action')
		});
	};

	// Wait until dom is ready
	$(document).ready(function(){
		$("#upload_files").click(changeOpacity);
		$("#launch_scenario").on('submit', function(e){launchScenario(e, $(this))});
		$("#form_update").hide();
		$("#update_description").click(function(e){$("#form_update").toggle()});

		if(scenarioStatus != "En cours"){
			$("#ajax-load").hide();
		}
	});

	$(document).ajaxComplete(function() {
		location.reload();
	});

}());
