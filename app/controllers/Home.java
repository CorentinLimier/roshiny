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

import java.util.List;
import java.util.Date;
import java.lang.Long;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
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
		return ok(index.render(projectName.value, form(CreateScenario.class), scenarios));
	}

	public Result createScenario() {
		Logger.info("Home.createScenario()");
		Setting projectName = Setting.find.byId("projectName"); 

		Form<CreateScenario> createForm = form(CreateScenario.class).bindFromRequest();

		if (createForm.hasErrors()) {
			Logger.error("Home.createScenario(): empty name");
			List<models.Scenario> scenarios = models.Scenario.find.all();
			return badRequest(index.render(projectName.value, createForm, scenarios));
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

		Logger.info("Home.createScenario(): OK");
		return redirect(routes.Scenario.index(newScenario.id));
	}

	public Result duplicateOrDeleteScenario(){
		Logger.info("Home.duplicateOrDeleteScenario()");

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
				scenario.delete();
			}			

			if(form.get().dupl_scenario.equals("dupl_scenario")){
				Logger.info("Home.duplicateOrDeleteScenario() duplicate "+ Long.toString(id));
				models.Scenario scenario = models.Scenario.find.byId(id); 
				models.Scenario newScenario = new models.Scenario();
				newScenario.name = scenario.name;
				newScenario.description = scenario.description;

				Context ctx = Context.current();
				String userName = ctx.session().get("user");
				if(!userName.isEmpty()){
					newScenario.user = User.find.where().eq("name", userName).findUnique();
				}

				newScenario.creationDate = new Date(); 
				newScenario.status = "Créé";
				newScenario.save();
				return redirect(routes.Scenario.index(newScenario.id));
			}
		}
		return redirect(routes.Home.index());
	}
}

