package controllers;

import play.*;
import play.mvc.*;
import play.data.*;

import models.*;
import views.html.*;

import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class Application extends Controller {

	public Result index() {
		Logger.info("Application.index()");
		Setting projectName = Setting.find.byId("projectName"); 
		return ok(index.render(projectName.value));
	}
}

