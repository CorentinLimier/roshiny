package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class DataFile extends Model{

	@Id @GeneratedValue
	public Long id;

	@OneToOne
	public File file;

	public String usage;
	public String name;
	public Boolean csvViz;
	public Boolean dataViz;

	public static Model.Finder<Long,DataFile> find = new Model.Finder<Long,DataFile>(
			Long.class, DataFile.class
	);
}
