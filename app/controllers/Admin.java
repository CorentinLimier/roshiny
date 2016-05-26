
package controllers;

import play.mvc.*;

import views.html.*;

public class Admin extends Controller {

	@Security.Authenticated(Secured.class)
	public Result index() {
		return ok(admin.render("Default Name"));
	}

	public Result logout() {
		session().clear();
		flash("success", "You've been logged out");
		return redirect(
				routes.Application.index()
		);
	}
}

