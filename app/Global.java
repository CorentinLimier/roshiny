import play.*;
import play.libs.*;

import models.ProjectSettings;

public class Global extends GlobalSettings {

	public void onStart(Application app) {
		ProjectSettings.readJsonFile("admin/projectSettings.json");
	}
}
