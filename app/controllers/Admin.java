package controllers;

import play.mvc.*;

import models.ProjectSettings;
import views.html.*;

public class Admin extends Controller {

	@Security.Authenticated(Secured.class)
	public Result index() {
		String applicationName_l = ProjectSettings.getApplicationName();
		return ok(admin.render(applicationName_l));
	}
}

