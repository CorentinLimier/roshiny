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
import java.lang.Long;
import java.util.Date;
import java.util.List;

public class Scenario extends Controller {

	public Result index(long scenarioId) {
		Logger.info("Scenario.index() " + Long.toString(scenarioId));
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

	public Result updateInformations(long scenarioId) {
		Logger.info("Scenario.updateInformations() " + Long.toString(scenarioId));

		Form<Home.CreateScenario> createForm = form(Home.CreateScenario.class).bindFromRequest();

		if (createForm.hasErrors()) {
			Logger.error("Scenario.updateInformations(): empty name");
			flash("error", "Erreur lors de la modification du scénario");
			return redirect(routes.Scenario.index(scenarioId));
		} 

		try{
			models.Scenario scenarioModel = models.Scenario.find.byId(scenarioId); 
			scenarioModel.name = createForm.get().scenario_name; 
			scenarioModel.description = createForm.get().scenario_description;
			scenarioModel.update();
		}
		catch(Exception exc){
			Logger.error("Scenario.updateInformations(): " + exc.getMessage());
			flash("error", "Erreur lors de la modification du scénario");
			return redirect(routes.Scenario.index(scenarioId));
		}

		return redirect(routes.Scenario.index(scenarioId));
	}

	public Result uploadFiles(long scenarioId) {
		Logger.info("Scenario.uploadFiles() " + Long.toString(scenarioId));

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

	public Result launch(long scenarioId) {
		Logger.info("Scenario.launch() " + Long.toString(scenarioId));

		String script = ParameterFile.find.byId("enginePath").file.path;
		String scenariosPath = ParameterFile.find.byId("scenariosPath").file.path;

		try {
			models.Scenario scenarioModel = models.Scenario.find.byId(scenarioId); 
			models.Run newRun = new models.Run();
			newRun.scenario = scenarioModel;
			newRun.runDate = new Date();
			scenarioModel.status = "En cours";
			scenarioModel.update();

			String scenarioPath = scenariosPath + "/" + Long.toString(scenarioId); 
			Process proc = Runtime.getRuntime().exec(script, null, new File(scenarioPath));

			long startTime = System.nanoTime();
			int exit = proc.waitFor();
			long endTime = System.nanoTime();

			if(exit!=0) {
				Logger.error("Scenario.launch() : exit status " + exit);
				newRun.success = false;
				scenarioModel.status = "Echec";
				newRun.duration = (endTime - startTime) / 1000000000.0; 
				newRun.save();
				scenarioModel.update();
				flash("error", "Erreur d'exécution du scénario");
				return redirect(routes.Scenario.index(scenarioId));
			}
			else {
				newRun.success = true;
				scenarioModel.status = "Succès";
				newRun.duration = (endTime - startTime) / 1000000000.0; 
				newRun.save();
				scenarioModel.update();
				Logger.info("Scenario.launch() : succès");
			}

		}
		catch(Exception exc){
			Logger.error("Scenario.launch() error : " + exc.getMessage());
			flash("error", "Erreur d'exécution du scénario");
			return redirect(routes.Scenario.index(scenarioId));
		}

		return redirect(routes.Scenario.index(scenarioId));
	}

	public Result downloadFile(long scenarioId, long dataFileId){
		Logger.info("Scenario.downloadFile() " + Long.toString(scenarioId) + "  " + Long.toString(dataFileId));
		String scenariosPath = ParameterFile.find.byId("scenariosPath").file.path;

		try {
			models.Scenario scenarioModel = models.Scenario.find.byId(scenarioId); 
			String scenarioPath = scenariosPath + "/" + Long.toString(scenarioId); 
			DataFile file = DataFile.find.byId(dataFileId);
			String filePath = scenarioPath + "/" + file.file.path;
			return ok(new File(filePath));
		}
		catch(Exception exc){
			Logger.error("Scenario.downloadFile() error : " + exc.getMessage());
			flash("error", "Erreur lors du téléchargement du fichier");
			return redirect(routes.Scenario.index(scenarioId));
		}
	}

	public Result downloadScenario(long scenarioId){
		Logger.info("Scenario.downloadScenario() " + Long.toString(scenarioId));
		String scenariosPath = ParameterFile.find.byId("scenariosPath").file.path;

		try {
			models.Scenario scenarioModel = models.Scenario.find.byId(scenarioId); 

			String script = "scripts/dashboard/compressScenario.sh";
			String args = " " + scenariosPath + " " + Long.toString(scenarioId); 

			Process proc = Runtime.getRuntime().exec(script + args);

			int exit = proc.waitFor();
			if(exit!=0) {
				Logger.error("Scenario.downloadScenario() : exit status " + exit);
				flash("error", "Erreur lors du téléchargement du scénario");
				return redirect(routes.Scenario.index(scenarioId));
			}
			else {
				Logger.info("Scenario.downloadScenario() : succès");
			}

			return ok(new File(scenariosPath + "/" + Long.toString(scenarioId) + ".tar.bz2"));
		}
		catch(Exception exc){
			Logger.error("Scenario.downloadScenario() error : " + exc.getMessage());
			flash("error", "Erreur lors du téléchargement du scénario");
			return redirect(routes.Scenario.index(scenarioId));
		}
	}
}
