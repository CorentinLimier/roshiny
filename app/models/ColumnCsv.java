/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class ColumnCsv extends Model {
	@Id @GeneratedValue
	public Long id;

	@ManyToOne
	public DataFile dataFile;

	public String name;
	public String columnType;
	public Integer position;

	public static Model.Finder<Long,ColumnCsv> find = new Model.Finder<Long,ColumnCsv>(
			Long.class, ColumnCsv.class
	);
}
