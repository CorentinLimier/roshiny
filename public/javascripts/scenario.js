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
	$(function(){
		$("#upload_files").click(changeOpacity);
	});

}());
