(function() {
	'use strict';

	var toggleSection = function(e){
		e.preventDefault();
		$("#scenarios_list").toggle();
		$("#new_scenario").toggle();
	};

	$(function(){
		// DataTables
		$("#new_scenario").toggle();
		$("#scenarios_table").DataTable({
			'pageLength': 50
		});

		// Sidebar
		$("#link_scenarios_list").click(toggleSection);
		$("#link_new_scenario").click(toggleSection);
	});
}());

