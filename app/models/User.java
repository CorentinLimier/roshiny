package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class User extends Model {

	@Id @GeneratedValue
	public Long id;

	@ManyToOne
	public Role role;

	public String name;
	public String password;

	public static Model.Finder<Long,User> find = new Model.Finder<Long,User>(
			Long.class, User.class
	);
}
