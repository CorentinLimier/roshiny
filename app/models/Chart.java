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
public class Chart extends Model {

	@Id @GeneratedValue
	public Long id;

	@ManyToOne
	public DataFile dataFile;

	public String typeChart;
	public String legend;

	public Integer position;
	public Integer height;
	public Integer width;

	public Integer absColumn;
	public String absTitle;
	public String absType;

	public Integer ordColumn;
	public String ordTitle;
	public String ordType;

	public String dateFormat;

	public boolean brush;

	public static Model.Finder<Long,Chart> find = new Model.Finder<Long,Chart>(
			Long.class, Chart.class
	);
}
