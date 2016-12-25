package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class ParameterFile extends Model{

	@Id
	public String parameter;

	@OneToOne
	public File file;

	public static Model.Finder<String,ParameterFile> find = new Model.Finder<String,ParameterFile>(
			String.class, ParameterFile.class
	);
}
