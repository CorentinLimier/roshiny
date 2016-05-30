package controllers;

import play.mvc.*;

import static play.data.Form.*;
import play.data.*;

import views.html.*;

public class LoginAdmin extends Controller {

	public static class Login {
		public String password;
		public String validate() {
			if (!password.equals("secret")){
				return "Invalid password";
			}
			return null;
		}
	}

	public Result login() {
		return ok(login.render("Default Name", form(Login.class)));
	}

	public Result authenticate() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(login.render("Default Name", loginForm));
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
