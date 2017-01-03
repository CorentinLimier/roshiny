/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import play.Logger;
import play.data.*;
import static play.data.Form.*;
import play.mvc.*;

import models.*;
import views.html.*;

@Security.Authenticated(SecuredAdmin.class)
public class AdminFile extends Controller {

	public static class Columns{
		public List<String> form_column_name = new ArrayList<String>();
		public List<String> form_column_type = new ArrayList<String>();

		public String validate() {
			for(String name: form_column_name){
				if(name.isEmpty()){
					return "Le champ \"Nom de la colonne\" est requis";
				}
			}
			return null;
		}
	}

	public static class ChartValidation{
		public String type_chart;
		public String name_chart;
		public Integer position_chart;
		public Integer height_chart;
		public Integer width_chart;
		public Integer abs_col_chart;
		public String abs_name_chart;
		public String abs_type_chart;
		public Integer ord_col_chart;
		public String ord_name_chart;
		public String ord_type_chart;
		public String abs_format_chart;
		public boolean brush_chart;

		public String validate() {
			if(name_chart.isEmpty()){
				return "Le champ \"Légende\" est requis";
			}
			return null;
		}
	}

	public static class ChartsUpdate{
		public List<Long> chart_id = new ArrayList<Long>();
		public List<String> chart_delete = new ArrayList<String>();
		public List<Integer> chart_position = new ArrayList<Integer>();
	}

	public Result configureDataFile(long dataFileId) {
		Logger.info("AdminFile.configureDataFile() " + Long.toString(dataFileId));
		HashMap<String, String> settings = new HashMap<String, String>();
		settings.put("projectName", Setting.find.byId("projectName").value);
		settings.put("datePickerFormat", Setting.find.byId("datePickerFormat").value);
		settings.put("enginePath", ParameterFile.find.byId("enginePath").file.path);
		settings.put("scenariosPath", ParameterFile.find.byId("scenariosPath").file.path);
		List<DataFile> dataInFiles = DataFile.find.where().eq("usage", "data-in").findList();
		List<DataFile> dataOutFiles = DataFile.find.where().eq("usage", "data-out").findList();

		try{
			DataFile dataFile = DataFile.find.byId(dataFileId);

			if(!(dataFile.csvViz || dataFile.dataViz)){
				Logger.error("AdminFile.configureDataFile(): file not configurable");
				flash("error", "Fichier non configurable");
				return redirect(routes.Admin.index());
			}

			List<ColumnCsv> columns = ColumnCsv.find.where().eq("dataFile", dataFile).findList();
			List<Chart> charts = Chart.find.where().eq("dataFile", dataFile).orderBy("position").findList();

			return ok(admin_file.render(settings, dataFile, dataInFiles, dataOutFiles, columns, charts));
		}
		catch(Exception exc){
			Logger.error("AdminFile.configureDataFile(): " + exc.getMessage());
			flash("error", "Fichier inconnu");
			return redirect(routes.Admin.index());
		}
	}

	public Result updateCsvViz(long dataFileId) {
		Logger.info("AdminFile.updateCsvViz() " + Long.toString(dataFileId));

		try{
			DataFile dataFile = DataFile.find.byId(dataFileId);

			if(!dataFile.csvViz){
				Logger.error("AdminFile.updateCsvViz(): pas csvViz");
				flash("error", "Fichier non configurable");
				return redirect(routes.Admin.index());
			}

			Form<Columns> form = form(Columns.class).bindFromRequest();
			if (form.hasErrors()) {
				String message = form.globalError().message();
				Logger.error("AdminFile.updateCsvViz(): " + message);
				flash("error", message);
				return redirect(routes.AdminFile.configureDataFile(dataFileId));
			} 

			List<ColumnCsv> columns = ColumnCsv.find.where().eq("dataFile", dataFile).findList();
			for(ColumnCsv column: columns){
				column.delete();
			}

			for(int i=0; i<form.get().form_column_name.size(); i++){
				String name = form.get().form_column_name.get(i);
				String columnType = form.get().form_column_type.get(i); 

				ColumnCsv newColumn = new ColumnCsv();
				newColumn.name = name;
				newColumn.dataFile = dataFile;
				newColumn.columnType = columnType;
				newColumn.save();
			}

			return redirect(routes.AdminFile.configureDataFile(dataFileId));
		}
		catch(Exception exc){
			Logger.error("AdminFile.updateCsvViz(): " + exc.getMessage());
			flash("error", "Fichier inconnu");
			return redirect(routes.Admin.index());
		}
	}

	public Result addChart(long dataFileId) {
		Logger.info("AdminFile.addChart() " + Long.toString(dataFileId));

		try{
			DataFile dataFile = DataFile.find.byId(dataFileId);

			if(!dataFile.dataViz){
				Logger.error("AdminFile.addChart(): not dataViz ");
				flash("error", "Fichier non configurable");
				return redirect(routes.Admin.index());
			}

			Form<ChartValidation> form = form(ChartValidation.class).bindFromRequest();
			if (form.hasErrors()) {
				String message = form.globalError().message();
				Logger.error("AdminFile.addChart(): " + message);
				flash("error", message);
				return redirect(routes.AdminFile.configureDataFile(dataFileId));
			} 

			Chart chart = new Chart();
			chart.dataFile = dataFile;
			chart.typeChart = form.get().type_chart;
			chart.legend = form.get().name_chart;
			chart.position = form.get().position_chart;
			chart.height = form.get().height_chart;
			chart.width = form.get().width_chart;
			chart.absColumn = form.get().abs_col_chart;
			chart.absTitle = form.get().abs_name_chart;
			chart.absType = form.get().abs_type_chart;
			chart.ordColumn = form.get().ord_col_chart;
			chart.ordTitle = form.get().ord_name_chart;
			chart.ordType = form.get().ord_type_chart;
			chart.dateFormat = form.get().abs_format_chart;
			chart.brush = form.get().brush_chart;

			chart.save();

			return redirect(routes.AdminFile.configureDataFile(dataFileId));
		}
		catch(Exception exc){
			Logger.error("AdminFile.addChart(): " + exc.getMessage());
			flash("error", "Fichier inconnu");
			return redirect(routes.Admin.index());
		}
	}

	public Result updateCharts(long dataFileId) {
		Logger.info("AdminFile.updateCharts() " + Long.toString(dataFileId));

		try{
			DataFile dataFile = DataFile.find.byId(dataFileId);

			if(!dataFile.dataViz){
				Logger.error("AdminFile.updateCharts(): not dataViz ");
				flash("error", "Fichier non configurable");
				return redirect(routes.Admin.index());
			}

			Form<ChartsUpdate> form = form(ChartsUpdate.class).bindFromRequest();
			if (form.hasErrors()) {
				String message = form.globalError().message();
				Logger.error("AdminFile.updateCharts(): " + message);
				flash("error", message);
				return redirect(routes.AdminFile.configureDataFile(dataFileId));
			} 

			for(int i=0; i<form.get().chart_id.size(); i++){
				Logger.debug(Long.toString(form.get().chart_id.get(i)));
				Logger.debug((form.get().chart_delete.get(i)));
				Logger.debug(Integer.toString(form.get().chart_position.get(i)));
				Chart chart = Chart.find.byId(form.get().chart_id.get(i));
				if(form.get().chart_delete.get(i).equals("true")){
					Logger.info("AdminFile.updateCharts(): delete chart" + Long.toString(chart.id));
					chart.delete();
				}
				else{
					chart.position = form.get().chart_position.get(i);
					chart.update();
				}
			}
			return redirect(routes.AdminFile.configureDataFile(dataFileId));
		}
		catch(Exception exc){
			Logger.error("AdminFile.updateCharts(): exception " + exc.getMessage());
			flash("error", "Fichier inconnu");
			return redirect(routes.Admin.index());
		}
	}
}
