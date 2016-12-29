/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.data.*;
import static play.data.Form.*;
import play.data.validation.Constraints;

import models.*;
import views.html.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Scenario extends Controller {

	public Result index(long scenarioId) {
		Logger.info("Scenario.index()");
		Setting projectName = Setting.find.byId("projectName"); 
		String scenariosPath = ParameterFile.find.byId("scenariosPath").file.path;

		try{
			models.Scenario scenarioModel = models.Scenario.find.byId(scenarioId); 
			List<DataFile> dataInFiles = DataFile.find.where().eq("usage", "data-in").findList();
			List<DataFile> dataOutFiles = DataFile.find.where().eq("usage", "data-out").findList();

			return ok(scenario.render(projectName.value, scenariosPath, scenarioModel, dataInFiles, dataOutFiles));
		}
		catch(Exception exc){
			flash("error", "Scénario inconnu");
			return redirect(routes.Home.index());
		}
	}

	public Result uploadFiles(long scenarioId) {
		Logger.info("Scenario.uploadFiles()");

		InputStream is = null;
		OutputStream os = null;

		String scenariosPath = ParameterFile.find.byId("scenariosPath").file.path;

		try{
			models.Scenario scenarioModel = models.Scenario.find.byId(scenarioId); 
			List<DataFile> dataInFiles = DataFile.find.where().eq("usage", "data-in").findList();

			MultipartFormData<File> body = request().body().asMultipartFormData();

			for(DataFile dataFile: dataInFiles){
				String id = Long.toString(dataFile.id);
				FilePart<File> filePart = body.getFile(id);

				if(filePart != null && !filePart.getFilename().isEmpty()){
					Logger.info("Scenario.uploadFiles() upload " + id);
					File source= filePart.getFile(); 
					File destination = new File(scenariosPath + "/" + Long.toString(scenarioModel.id) + "/" + dataFile.file.path); 

					is = new FileInputStream(source);
					os = new FileOutputStream(destination);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}
					is.close();
					os.close();
				}
			}
		}
		catch(Exception exc){
			Logger.error("Scenario.uploadFiles() error : " + exc.getMessage());
			flash("error", "Erreur lors de l'upload");
			return redirect(routes.Scenario.index(scenarioId));
		}

		return redirect(routes.Scenario.index(scenarioId));
	}
}
