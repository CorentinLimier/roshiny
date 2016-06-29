package controllers;

import play.mvc.*;

import static play.data.Form.*;
import play.data.*;

import models.ProjectSettings;
import views.html.*;

public class LoginAdmin extends Controller {

	public static class Login {
		public String password;

		public String validate() {
			String adminPassword_l = ProjectSettings.getAdminPassword();
			if (!password.equals(adminPassword_l)){
				return "Invalid password";
			}
			return null;
		}
	}

	public Result login() {
		String applicationName_l = ProjectSettings.getApplicationName();
		return ok(login.render(applicationName_l, form(Login.class)));
	}

	public Result authenticate() {
		Form<Login> loginForm_l = form(Login.class).bindFromRequest();
		String applicationName_l = ProjectSettings.getApplicationName();
		if (loginForm_l.hasErrors()) {
			return badRequest(login.render(applicationName_l, loginForm_l));
		} 
		else {
			session().clear();
			session("user", "admin");
			return redirect(
					routes.Admin.index()
					);
		}
	}

	public Result logout() {
		session().clear();
		flash("success", "You've been logged out");
		return redirect(
				routes.Application.index()
		);
	}
}
