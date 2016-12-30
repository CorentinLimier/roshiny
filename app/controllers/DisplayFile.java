/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import models.*;
import views.html.*;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Long;
import java.util.Date;
import java.util.List;

public class DisplayFile extends Controller {

	public Result display(long scenarioId, long dataFileId){
		Logger.info("DisplayFile.display() " + Long.toString(scenarioId) + Long.toString(dataFileId));
		Setting projectName = Setting.find.byId("projectName"); 
		String scenariosPath = ParameterFile.find.byId("scenariosPath").file.path;

		try{
			models.Scenario scenarioModel = models.Scenario.find.byId(scenarioId); 
			DataFile dataFile = DataFile.find.byId(dataFileId);
			List<ColumnCsv> columns = ColumnCsv.find.where().eq("dataFile", dataFile).findList();
			List<DataFile> dataInFiles = DataFile.find.where().eq("usage", "data-in").findList();
			List<DataFile> dataOutFiles = DataFile.find.where().eq("usage", "data-out").findList();

			File file = new File(scenariosPath + "/" + scenarioId + "/" + dataFile.file.path);

			String fileContent = new String();

			if(file.exists()){
				FileReader filereader = new FileReader(file);
				BufferedReader br = new BufferedReader(filereader);

				String line = null;

				while ((line = br.readLine()) != null){
					fileContent += line + "\n";
				}
			}

			return ok(display_file.render(projectName.value, scenariosPath, scenarioModel, dataFile, fileContent.trim(), dataInFiles, dataOutFiles, columns));
		}
		catch(Exception exc){
			Logger.info("DisplayFile.display() " + exc.getMessage());
			flash("error", "Scénario ou fichier inconnu");
			return redirect(routes.Home.index());
		}
	}

	public Result updateFile(long scenarioId, long dataFileId){
		Logger.info("DisplayFile.updateFile() " + Long.toString(scenarioId) + Long.toString(dataFileId));
		String scenariosPath = ParameterFile.find.byId("scenariosPath").file.path;

		try{
			models.Scenario scenarioModel = models.Scenario.find.byId(scenarioId); 
			DataFile dataFile = DataFile.find.byId(dataFileId);

			DynamicForm form = Form.form().bindFromRequest();	
			String fileContent = form.get("file_content");

			File file = new File(scenariosPath + "/" + scenarioId + "/" + dataFile.file.path);

			if(file.exists()){
				file.delete();
			}

			FileWriter filewriter = new FileWriter(file, true);
			BufferedWriter out = new BufferedWriter(filewriter);
			out.write(fileContent);
			out.close();

			return redirect(routes.DisplayFile.display(scenarioId, dataFileId));
		}
		catch(Exception exc){
			Logger.info("DisplayFile.updateFile() " + exc.getMessage());
			flash("error", "Erreur lors de la modification du fichier");
			return redirect(routes.DisplayFile.display(scenarioId, dataFileId));
		}
	}
}
