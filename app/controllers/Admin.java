package controllers;

import play.mvc.*;

import models.*;
import views.html.*;

@Security.Authenticated(SecuredAdmin.class)
public class Admin extends Controller {

	public Result index() {
		Setting projectName = Setting.find.byId("projectName"); 
		return ok(admin.render(projectName.value));
	}
}

