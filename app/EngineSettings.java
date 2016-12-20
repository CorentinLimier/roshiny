package models;

import play.*;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/* Singleton */
public class EngineSettings
{	

	private static String pathLaunchScript_m;
	private static String pathScenariiDir_m;
	private static String relativePathDataIn_m;
	private static String relativePathDataOut_m;

	public static void setPathLaunchScript(String name_p){
		pathLaunchScript_m = name_p;
	}

	public static String getPathLaunchScript(){
		return pathLaunchScript_m;
	}

	public static void setPathScenariiDir(String password_p){
		pathScenariiDir_m = password_p;
	}

	public static String getPathScenariiDir(){
		return pathScenariiDir_m;
	}

	public static void setRelativePathDataIn(String password_p){
		relativePathDataIn_m = password_p;
	}

	public static String getRelativePathDataIn(){
		return relativePathDataIn_m;
	}

	public static void setRelativePathDataOut(String password_p){
		relativePathDataOut_m = password_p;
	}

	public static String getRelativePathDataOut(){
		return relativePathDataOut_m;
	}

	public static void readJsonFile(String path_p)
	{
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(new File(path_p));
		
			setPathLaunchScript(root.findPath("pathLaunchScript").textValue());
			setPathScenariiDir(root.findPath("pathScenariiDir").textValue());
			setRelativePathDataIn(root.findPath("relativePathDataIn").textValue());
			setRelativePathDataOut(root.findPath("relativePathDataOut").textValue());
		}
		catch(Exception e)
		{
			Logger.error(e.toString());
		}
	}

	public static void writeJsonFile(String content_p, String path_p){
	}
}
