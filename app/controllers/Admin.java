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
public class Admin extends Controller {

	public static class Password{
		public String act_pswd;
		public String new_pswd;
		public String conf_pswd;

		public String validate() {
			String adminPassword = User.find.where().eq("name", "admin").findUnique().password;
			if (!act_pswd.equals(adminPassword)){
				return "Mot de passe actuel invalide";
			}
			if(!new_pswd.equals(conf_pswd)){
				return "Confirmation invalide";
			}
			return null;
		}
	}

	public static class FileValidation{
		public String path;

		public String validate() {
			File f = new File(path);
			if(f.exists()){
				return null;
			}
			else{
				return "Path does not exist";
			}
		}
	}

	public static class DataFileValidation{
		public List<String> form_file_name;
		public List<String> form_file_path;
		public List<Boolean> form_csv_viz;
		public List<Boolean> form_ignore_header;
		public List<Boolean> form_data_viz;

		public String validate() {
			return null;
		}
	}

	public Result index() {
		Logger.info("Admin.index()");
		HashMap<String, String> settings = new HashMap<String, String>();
		settings.put("projectName", Setting.find.byId("projectName").value);
		settings.put("enginePath", ParameterFile.find.byId("enginePath").file.path);
		settings.put("scenariosPath", ParameterFile.find.byId("scenariosPath").file.path);
		List<DataFile> dataInFiles = DataFile.find.where().eq("usage", "data-in").findList();
		List<DataFile> dataOutFiles = DataFile.find.where().eq("usage", "data-out").findList();
		return ok(admin.render(settings, dataInFiles, dataOutFiles));
	}

	public Result setApplicationName() {
		try{
			DynamicForm form = Form.form().bindFromRequest();	
			String newName = form.get("app_name");
			Logger.info("Admin.setApplicationName(): " + newName);
			Setting projectName = Setting.find.byId("projectName"); 
			projectName.value = newName;
			projectName.update();
			return ok("OK");
		}
		catch(Exception exc){
			Logger.error("Admin.setApplicationName: "+exc);
			return badRequest("Erreur");
		}
	}

	public Result setPassword() {
		Logger.info("Admin.setPassword()");
		Form<Password> passwordForm = form(Password.class).bindFromRequest();

		if (passwordForm.hasErrors()) {
			Logger.error("Admin.setPassword(): "+passwordForm.globalError().message());
			return badRequest(passwordForm.globalError().message());
		} 

		User admin = User.find.where().eq("name", "admin").findUnique();
		admin.password = passwordForm.get().new_pswd;
		admin.update();

		Logger.info("Admin.setPassword(): password updated");

		return ok("OK");
	}

	public Result setEnginePath() {
		Logger.info("Admin.setEnginePath()");
		Form<FileValidation> fileForm = form(FileValidation.class).bindFromRequest();

		if (fileForm.hasErrors()) {
			Logger.error("Admin.setEnginePath(): " + fileForm.globalError().message());
			return badRequest(fileForm.globalError().message());
		} 

		String newPath = fileForm.get().path;

		models.File engineFile = ParameterFile.find.byId("enginePath").file;
		engineFile.path = newPath;
		engineFile.update();

		Logger.info("Admin.setEnginePath(): " + newPath);

		return ok("OK");
	}

	public Result setScenariosPath() {
		Logger.info("Admin.setScenariosPath()");
		Form<FileValidation> fileForm = form(FileValidation.class).bindFromRequest();

		if (fileForm.hasErrors()) {
			Logger.error("Admin.setScenariosPath(): " + fileForm.globalError().message());
			return badRequest(fileForm.globalError().message());
		} 

		String newPath = fileForm.get().path;

		models.File scenariosFile = ParameterFile.find.byId("scenariosPath").file;
		scenariosFile.path = newPath;
		scenariosFile.update();

		Logger.info("Admin.setScenariosPath(): " + newPath);

		return ok("OK");
	}

	public Result setDataInFiles(){
		Logger.info("Admin.setDataInFiles()");
		setDataFiles("data-in");
		return index();
	}

	public Result setDataOutFiles(){
		Logger.info("Admin.setDataOutFiles()");
		setDataFiles("data-out");
		return index();
	}

	private void setDataFiles(String usage){
		List<DataFile> dataInFiles = DataFile.find.where().eq("usage", usage).findList();
		for(DataFile dataFile: dataInFiles){
			dataFile.file.delete();
			dataFile.delete();
		}

		try{
			Form<DataFileValidation> form = form(DataFileValidation.class).bindFromRequest();

			for(int i=0; i<form.get().form_file_name.size(); i++){
				models.File file = new models.File();
				file.path = form.get().form_file_path.get(i);
				DataFile dataFile = new DataFile();
				dataFile.file = file;
				dataFile.usage = usage;
				dataFile.name = form.get().form_file_name.get(i);
				dataFile.csvViz = form.get().form_csv_viz.get(i);
				dataFile.ignoreHeader = form.get().form_ignore_header.get(i);
				dataFile.dataViz = form.get().form_data_viz.get(i);
				file.save();
				dataFile.save();
			}
		}
		catch(Exception exc){
		}
	}
}
