/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

package controllers;

import java.io.File;
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

			return ok(admin_file.render(settings, dataFile, dataInFiles, dataOutFiles));
		}
		catch(Exception exc){
			Logger.error("AdminFile.configureDataFile(): " + exc.getMessage());
			flash("error", "Fichier inconnu");
			return redirect(routes.Admin.index());
		}
	}
}
