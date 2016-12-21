package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Role extends Model {

	@Id @GeneratedValue
	public Long id;

	public String name;

	public static Model.Finder<Long,Role> find = new Model.Finder<Long,Role>(
			Long.class, Role.class
	);
}
