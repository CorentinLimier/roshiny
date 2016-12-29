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

	// Wait until dom is ready
	$(document).ready(function(){
		$("#upload_1files").click(changeOpacity);
		$("#launch").click(changeOpacity);
		$("#form_update").hide();
		$("#update_description").click(function(e){$("#form_update").toggle()});
	});

}());
