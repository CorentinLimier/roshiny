package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class File extends Model{

	@Id @GeneratedValue
	public Long id;

	public String path;

	public static Model.Finder<Long,File> find = new Model.Finder<Long,File>(
			Long.class, File.class
	);
}
