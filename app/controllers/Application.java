package controllers;

import play.mvc.*;

import static play.data.Form.*;
import play.data.*;

import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class Application extends Controller {

	/**
	 * An action that renders an HTML page with a welcome message.
	 * The configuration in the <code>routes</code> file means that
	 * this method will be called when the application receives a
	 * <code>GET</code> request with a path of <code>/</code>.
	 */

	public static class Login {
		public String password;
		public String validate() {
			if (!password.equals("secret")){
				return "Invalid user or password";
			}
			return null;
		}
	}

	public Result index() {
		return ok(index.render("Default Name"));
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
}

