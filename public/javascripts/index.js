(function() {
	'use strict';

	var toggleSection = function(e){
		var id_link = $(this).attr("id");
		var section = id_link.substring(5, id_link.length);
		e.preventDefault();
		$(".section").hide();
		$("#" + section).show();
	};

	$(function(){
		// DataTables
		$("#scenarios_table").DataTable({
			'pageLength': 50
		});

		// Sections
		$(".section").hide();
		$("#scenarios_list").show();
		$(".link_section").click(toggleSection);
	});
}());

