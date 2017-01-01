/*
MIT License
Copyright (c) 2016 Corentin Limier 
See LICENSE file at root of project for more informations
*/

/*
create table role (
  id                            integer not null,
  name                          varchar(255),
  constraint pk_role primary key (id)
);

insert into role values (1, "admin");

create table setting (
  name                          varchar(255) not null,
  value                         varchar(255),
  constraint pk_setting primary key (name)
);

insert into setting values ("projectName", "ROShiny");
insert into setting values ("datePickerFormat", "yyyy-mm-dd");
insert into setting values ("csvSeparator", ";");

create table user (
  id                            integer not null,
  role_id                       integer,
  name                          varchar(255),
  password                      varchar(255),
  constraint pk_user primary key (id)
);

insert into user values (1, 1, "admin", "");

create table data_file (
  id                            integer not null,
  file_id                       integer,
  name							varchar(255),
  usage							varchar(255),
  csv_viz                       integer(1),
  ignore_header                 integer(1),
  data_viz                      integer(1),
  constraint uq_data_file_file_id unique (file_id),
  constraint pk_data_file primary key (id)
);

create table file (
  id                            integer not null,
  path                          varchar(255),
  constraint pk_file primary key (id)
);

insert into file values (1, "");
insert into file values (2, "");

create table parameter_file (
  parameter                     varchar(255) not null,
  file_id                       integer,
  constraint uq_parameter_file_file_id unique (file_id),
  constraint pk_parameter_file primary key (parameter)
);

insert into parameter_file values ("enginePath", 1);
insert into parameter_file values ("scenariosPath", 2);
*/

create table scenario (
  id                            integer not null,
  user_id                       integer,
  name                          varchar(255),
  description                   TEXT,
  creation_date                 timestamp,
  status                        varchar(255),
  constraint pk_scenario primary key (id)
);

/*
create table run (
  id                            integer not null,
  scenario_id                   integer,
  run_date						timestamp,
  duration                      double,
  success                       integer(1),
  constraint pk_run primary key (id)
);

create table column_csv (
  id                            integer not null,
  data_file_id                  integer,
  name                          varchar(255),
  column_type					varchar(255),
  position						integer,
  constraint pk_column_csv primary key (id)
);
*/
