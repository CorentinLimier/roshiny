package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Setting extends Model {

	@Id
	public String name;
	public String value;

	public static Model.Finder<String,Setting> find = new Model.Finder<String,Setting>(
			String.class, Setting.class
	);
}
