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
		return redirect(routes.Scenario.index(newScenario.name));
	}
}

