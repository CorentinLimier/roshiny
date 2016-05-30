
package controllers;

import play.mvc.*;

import views.html.*;

public class Admin extends Controller {

	@Security.Authenticated(Secured.class)
	public Result index() {
		return ok(admin.render("Default Name"));
	}
}

