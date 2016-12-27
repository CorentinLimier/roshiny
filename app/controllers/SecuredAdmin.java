/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;

import models.*;

public class SecuredAdmin extends Security.Authenticator {

	@Override
	public String getUsername(Context ctx) {
		String userName = ctx.session().get("user");
		User user = User.find.where().eq("name", ctx.session().get("user")).findUnique();
		if(user == null){
			return null;
		}
		if(user.role.name.equals("admin")){
			return userName;
		}
		return null;
	}

	@Override
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.LoginAdmin.login());
	}
}
