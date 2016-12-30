/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;
import play.data.*;
import static play.data.Form.*;
import play.data.validation.Constraints;

import models.*;
import views.html.*;

import java.io.File;
import java.lang.Long;
import java.util.List;
import java.util.Date;


public class Home extends Controller {

	public static class CreateScenario{
		public String scenario_name = "default";
		public String scenario_description;

		public String validate() {
			if (scenario_name.isEmpty()){
				return "Le champ nom est requis";
			}
			return null;
		}
	}

	public static class DuplOrDel{
		public List<Long> selected_scenario; 
		public String dupl_scenario = "";
		public String del_scenario = "";
	}

	public Result index() {
		Logger.info("Home.index()");
		Setting projectName = Setting.find.byId("projectName"); 
		List<models.Scenario> scenarios = models.Scenario.find.all();
		return ok(index.render(projectName.value, scenarios));
	}

	private void createScenarioDirectories(models.Scenario scenario) throws Exception{
		String scenariosPath = ParameterFile.find.byId("scenariosPath").file.path;

		List<DataFile> dataFiles = DataFile.find.all();

		for(DataFile dataFile: dataFiles){
			File dfile = new File(dataFile.file.path);
			String path = scenariosPath + "/" + Long.toString(scenario.id) + "/" + dfile.getParent();
			File file = new File(path);
			if(file.exists() && file.isDirectory()){
				continue;
			}
			Logger.info("Home.createScenarioDirectories() " + path);
			if(!file.mkdirs()){
				throw new Exception("Cannot create directory " + path);
			}
		}
	}

	private boolean deleteDirectory(File dir){
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDirectory(new File(dir, children[i]));

				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	private void deleteScenarioDirectories(models.Scenario scenario) throws Exception{
		String scenariosPath = ParameterFile.find.byId("scenariosPath").file.path + "/" + Long.toString(scenario.id);
		Logger.info("Home.deleteScenarioDirectories() " + scenariosPath);

		if(!deleteDirectory(new File(scenariosPath))){
			throw new Exception("Cannot delete directory " + scenariosPath);
		}
	} 

	public Result createScenario() {
		Logger.info("Home.createScenario()");
		Setting projectName = Setting.find.byId("projectName"); 

		Form<CreateScenario> createForm = form(CreateScenario.class).bindFromRequest();

		if (createForm.hasErrors()) {
			Logger.error("Home.createScenario(): empty name");
			flash("error", "Erreur lors de la création du scénario");
			return redirect(routes.Home.index());
		} 

		models.Scenario newScenario = new models.Scenario();
		newScenario.name = createForm.get().scenario_name;
		newScenario.description = createForm.get().scenario_description;

		Context ctx = Context.current();
		String userName = ctx.session().get("user");
		if(!userName.isEmpty()){
			newScenario.user = User.find.where().eq("name", userName).findUnique();
		}

		newScenario.creationDate = new Date(); 
		newScenario.status = "Créé";
		newScenario.save();

		try{
			createScenarioDirectories(newScenario);
		}
		catch(Exception exc){
			Logger.error("Home.createScenario(): " + exc.getMessage());
			newScenario.delete();
			flash("error", "Erreur lors de la création du scénario");
			return redirect(routes.Home.index());
		}

		Logger.info("Home.createScenario(): OK");
		return redirect(routes.Scenario.index(newScenario.id));
	}

	public Result duplicateOrDeleteScenario(){
		Logger.info("Home.duplicateOrDeleteScenario()");

		Setting projectName = Setting.find.byId("projectName"); 

		Form<DuplOrDel> form = form(DuplOrDel.class).bindFromRequest();

		if (form.hasErrors()) {
			Logger.error("Home.duplicateOrDeleteScenario() error form");
			return redirect(routes.Home.index());
		} 

		for(int i=0; i<form.get().selected_scenario.size(); i++){
			Long id = form.get().selected_scenario.get(i);

			if(form.get().del_scenario.equals("del_scenario")){
				Logger.info("Home.duplicateOrDeleteScenario() delete "+ Long.toString(id));

				models.Scenario scenario = models.Scenario.find.byId(id); 

				try{
					deleteScenarioDirectories(scenario);
					scenario.delete();
				}
				catch(Exception exc){
					Logger.error("Home.duplicateOrDeleteScenario(): " + exc.getMessage());
					flash("error", "Erreur lors de la suppression des scénarios");
					return redirect(routes.Home.index());
				}
			}			

			if(form.get().dupl_scenario.equals("dupl_scenario")){
				Logger.info("Home.duplicateOrDeleteScenario() duplicate "+ Long.toString(id));
				models.Scenario scenario = models.Scenario.find.byId(id); 
				models.Scenario newScenario = new models.Scenario();
				newScenario.name = scenario.name + " [dupl.] ";
				newScenario.description = scenario.description;

				Context ctx = Context.current();
				String userName = ctx.session().get("user");

				if(!userName.isEmpty()){
					newScenario.user = User.find.where().eq("name", userName).findUnique();
				}

				newScenario.creationDate = new Date(); 
				newScenario.status = "Créé";
				newScenario.save();

				try{
					String scenariosPath = ParameterFile.find.byId("scenariosPath").file.path;
					String script = "scripts/dashboard/duplicateScenario.sh";
					String args = " " + scenariosPath + " " + Long.toString(scenario.id) + " " + Long.toString(newScenario.id);

					Process proc = Runtime.getRuntime().exec(script + args);

					int exit = proc.waitFor();
					if(exit!=0) {
						Logger.error("Scenario.duplicateOrDeleteScenario() : duplicate exit status" + exit);
						flash("error", "Erreur lors de la duplication du scénario");
						newScenario.delete();
						return redirect(routes.Home.index());
					}
				}
				catch(Exception exc){
					Logger.error("Home.duplicateOrDeleteScenario(): " + exc.getMessage());
					newScenario.delete();
					flash("error", "Erreur lors de la duplication du scénario");
					return redirect(routes.Home.index());
				}

				return redirect(routes.Scenario.index(newScenario.id));
			}
		}

		return redirect(routes.Home.index());
	}
}

