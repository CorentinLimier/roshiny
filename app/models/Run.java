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
public class Run extends Model {

	@Id @GeneratedValue
	public Long id;

	@ManyToOne
	public Scenario scenario;

	@Formats.DateTime(pattern="dd/MM/yyyy")
	public Date creationDate;

	// duration in seconds
	public Long duration;

	public Boolean success;

	public static Model.Finder<Long,Run> find = new Model.Finder<Long,Run>(
			Long.class, Run.class
	);
}
