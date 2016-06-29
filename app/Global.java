import play.*;
import play.libs.*;

import models.ProjectSettings;

public class Global extends GlobalSettings {

	public void onStart(Application app) {
		ProjectSettings.setApplicationName("Hello Name");
		ProjectSettings.setAdminPassword("hellopassword");
	}
}
