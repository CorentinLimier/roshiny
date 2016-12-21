package controllers;

import play.mvc.*;

import models.*;
import views.html.*;

public class Admin extends Controller {

	@Security.Authenticated(Secured.class)
	public Result index() {
		Setting projectName = Setting.find.byId("projectName"); 
		return ok(admin.render(projectName.value));
	}
}

