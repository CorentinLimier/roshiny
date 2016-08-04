package models;

import play.*;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	public static void readJsonFile(String path_p)
	{
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(new File(path_p));
		
			setApplicationName(root.findPath("applicationName").textValue());
			setAdminPassword(root.findPath("adminPassword").textValue());
		}
		catch(Exception e)
		{
			Logger.error(e.toString());
		}
	}

	public static void writeJsonFile(String content_p, String path_p){
	}
}
