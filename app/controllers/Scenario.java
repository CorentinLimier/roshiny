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

	public Result index(String scenarioName) {
		Logger.info("Scenario.index()");
		Setting projectName = Setting.find.byId("projectName"); 
		return ok(scenario.render(projectName.value, scenarioName));
	}
}
