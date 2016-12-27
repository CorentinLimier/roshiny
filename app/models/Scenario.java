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
public class Scenario extends Model {

	@Id @GeneratedValue
	public Long id;

	@ManyToOne
	public User user;

	public String name;

	@Column(columnDefinition = "TEXT")
	public String description;

	@Formats.DateTime(pattern="dd/MM/yyyy")
	public Date creationDate;

	@Formats.DateTime(pattern="dd/MM/yyyy")
	public Date lastRunDate;

	public String status;

	public static Model.Finder<Long,Scenario> find = new Model.Finder<Long,Scenario>(
			Long.class, Scenario.class
	);
}
