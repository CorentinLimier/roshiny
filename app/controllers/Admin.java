package controllers;

import play.Logger;
import play.data.*;
import play.mvc.*;

import models.*;
import views.html.*;

@Security.Authenticated(SecuredAdmin.class)
public class Admin extends Controller {

	public Result index() {
		Setting projectName = Setting.find.byId("projectName"); 
		return ok(admin.render(projectName.value));
	}

	public Result setApplicationName() {
		try{
			DynamicForm form = Form.form().bindFromRequest();	
			String newName = form.get("app_name");
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
		return ok("OK");
	}
}

