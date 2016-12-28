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
			List<DataFile> dataInFiles = DataFile.find.where().eq("usage", "data-in").findList();
			List<DataFile> dataOutFiles = DataFile.find.where().eq("usage", "data-out").findList();

			return ok(scenario.render(projectName.value, scenar, dataInFiles, dataOutFiles));
		}
		catch(Exception exc){
			return redirect(routes.Home.index());
		}
	}
}
