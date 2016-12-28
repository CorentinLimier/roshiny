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
import play.data.validation.Constraints;

import models.*;
import views.html.*;

import java.util.List;

public class Scenario extends Controller {

	public Result index(long scenarioId) {
		Logger.info("Scenario.index()");
		Setting projectName = Setting.find.byId("projectName"); 
		try{
			models.Scenario scenar = models.Scenario.find.byId(scenarioId); 
			return ok(scenario.render(projectName.value, scenar));
		}
		catch(Exception exc){
			return redirect(routes.Home.index());
		}
	}
}
