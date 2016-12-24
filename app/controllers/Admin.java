package controllers;

import play.Logger;
import play.data.*;
import static play.data.Form.*;
import play.mvc.*;

import models.*;
import views.html.*;

@Security.Authenticated(SecuredAdmin.class)
public class Admin extends Controller {

	public static class Password{
		public String act_pswd;
		public String new_pswd;
		public String conf_pswd;

		public String validate() {
			String adminPassword = User.find.where().eq("name", "admin").findUnique().password;
			if (!act_pswd.equals(adminPassword)){
				return "Mot de passe actuel invalide";
			}
			if(!new_pswd.equals(conf_pswd)){
				return "Confirmation invalide";
			}
			return null;
		}
	}

	public Result index() {
		Logger.info("Admin.index()");
		Setting projectName = Setting.find.byId("projectName"); 
		return ok(admin.render(projectName.value));
	}

	public Result setApplicationName() {
		try{
			DynamicForm form = Form.form().bindFromRequest();	
			String newName = form.get("app_name");
			Logger.info("Admin.setApplicationName(): " + newName);
			Setting projectName = Setting.find.byId("projectName"); 
			projectName.value = newName;
			projectName.update();
			return ok("OK");
		}
		catch(Exception exc){
			Logger.error("Admin.setApplicationName: "+exc);
			return badRequest("Erreur");
		}
	}

	public Result setPassword() {
		Logger.info("Admin.setPassword()");
		Form<Password> passwordForm = form(Password.class).bindFromRequest();

		if (passwordForm.hasErrors()) {
			Logger.info("Admin.setPassword(): "+passwordForm.globalError().message());
			return badRequest(passwordForm.globalError().message());
		} 

		User admin = User.find.where().eq("name", "admin").findUnique();
		admin.password = passwordForm.get().new_pswd;
		admin.update();

		Logger.info("Admin.setPassword(): password updated");

		return ok("OK");
	}
}

