/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

(function() {
	'use strict';

	var toggleSection = function(e){
		var id_link = $(this).attr("id");
		var section = id_link.substring(5, id_link.length);
		e.preventDefault();
		$(".section").hide();
		$("#" + section).show();
	};

	var lockDelDupl = function(e){
		var table = $(this).closest("table");
		var nbChecked = table.find("input:checked").length;
		if(nbChecked == 0){
			$("#del_scenario").attr("disabled", true);
			$("#dupl_scenario").attr("disabled", true);
		}
		else if(nbChecked == 1){
			$("#del_scenario").attr("disabled", false);
			$("#dupl_scenario").attr("disabled", false);
		}
		else{
			$("#del_scenario").attr("disabled", false);
			$("#dupl_scenario").attr("disabled", true);
		}
	}

	var alertDelete = function(e){
		var r = confirm("Etes-vous sûr de vouloir supprimer les scénarios sélectionnés ?");
		if(!r){
			e.preventDefault();
		}
	}

	$(function(){
		// DataTables
		$("#scenarios_table").DataTable({
			'pageLength': 50
		}).draw();


		// Sections
		$(".section").hide();
		$("#scenarios_list").show();
		$(".link_section").click(toggleSection);

		// Lock del_scenario & dupl_scenario buttons 
		$(".select_scenario").click(lockDelDupl);
		$("#del_scenario").attr("disabled", true);
		$("#dupl_scenario").attr("disabled", true);

		// Alert if delete scenarios
		$("#del_scenario").click(alertDelete);
	});
}());

