package controllers;

import play.mvc.*;

import static play.data.Form.*;
import play.data.*;

import models.*;
import views.html.*;

public class LoginAdmin extends Controller {

	public static class Login {
		public String password;

		public String validate() {
			String adminPassword = User.find.all().get(0).password;
			if (!password.equals(adminPassword)){
				return "Invalid password";
			}
			return null;
		}
	}

	public Result login() {
		Setting projectName = Setting.find.byId("projectName"); 
		return ok(login.render(projectName.value, form(Login.class)));
	}

	public Result authenticate() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		Setting projectName = Setting.find.byId("projectName"); 
		if (loginForm.hasErrors()) {
			return badRequest(login.render(projectName.value, loginForm));
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
