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
				Logger.error("AdminFile.configureDataFile(): ni csvViz ni dataViz ");
				flash("error", "Fichier non configurable");
				return redirect(routes.Admin.index());
			}

			List<ColumnCsv> columns = ColumnCsv.find.where().eq("dataFile", dataFile).findList();

			return ok(admin_file.render(settings, dataFile, dataInFiles, dataOutFiles, columns));
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

			if(!(dataFile.csvViz || dataFile.dataViz)){
				Logger.error("AdminFile.updateCsvViz(): ni csvViz ni dataViz ");
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
}
