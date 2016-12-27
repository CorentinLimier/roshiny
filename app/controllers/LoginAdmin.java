/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

package controllers;

import play.Logger;
import play.mvc.*;

import static play.data.Form.*;
import play.data.*;

import models.*;
import views.html.*;

public class LoginAdmin extends Controller {

	public static class Login {
		public String password;

		public String validate() {
			String adminPassword = User.find.where().eq("name", "admin").findUnique().password;
			if (!password.equals(adminPassword)){
				return "Mot de passe invalide";
			}
			return null;
		}
	}

	public Result login() {
		Logger.info("LoginAdmin.login()");
		Setting projectName = Setting.find.byId("projectName"); 
		return ok(login.render(projectName.value, form(Login.class)));
	}

	public Result authenticate() {
		Logger.info("LoginAdmin.authenticate()");
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		Setting projectName = Setting.find.byId("projectName"); 

		if (loginForm.hasErrors()) {
			Logger.error("LoginAdmin.authenticate(): invalid password");
			return badRequest(login.render(projectName.value, loginForm));
		} 

		Logger.info("LoginAdmin.authenticate(): password OK");
		session().clear();
		session("user", "admin");
		return redirect(routes.Admin.index());
	}

	public Result logout() {
		Logger.info("LoginAdmin.logout()");
		session().clear();
		flash("success", "Vous avez été déconnecté");
		return redirect(
				routes.Home.index()
		);
	}
}
