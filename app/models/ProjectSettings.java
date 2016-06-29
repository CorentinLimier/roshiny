package models;

/* Singleton */
public class ProjectSettings
{	

	private static String applicationName_m;
	private static String adminPassword_m;

	public static void setApplicationName(String name_p){
		applicationName_m = name_p;
	}

	public static String getApplicationName(){
		return applicationName_m;
	}

	public static void setAdminPassword(String password_p){
		adminPassword_m = password_p;
	}

	public static String getAdminPassword(){
		return adminPassword_m;
	}
}
