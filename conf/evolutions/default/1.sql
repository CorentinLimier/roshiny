# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table column_csv (
  id                            integer autoincrement not null,
  data_file_id                  integer,
  name                          varchar(255),
  column_type                   varchar(255),
  position                      integer,
  constraint pk_column_csv primary key (id)
);

create table data_file (
  id                            integer autoincrement not null,
  file_id                       integer,
  usage                         varchar(255),
  name                          varchar(255),
  csv_viz                       integer(1),
  ignore_header                 integer(1),
  data_viz                      integer(1),
  constraint uq_data_file_file_id unique (file_id),
  constraint pk_data_file primary key (id)
);

create table file (
  id                            integer autoincrement not null,
  path                          varchar(255),
  constraint pk_file primary key (id)
);

create table parameter_file (
  parameter                     varchar(255) not null,
  file_id                       integer,
  constraint uq_parameter_file_file_id unique (file_id),
  constraint pk_parameter_file primary key (parameter)
);

create table role (
  id                            integer autoincrement not null,
  name                          varchar(255),
  constraint pk_role primary key (id)
);

create table run (
  id                            integer autoincrement not null,
  scenario_id                   integer,
  run_date                      timestamp,
  duration                      double,
  success                       integer(1),
  constraint pk_run primary key (id)
);

create table scenario (
  id                            integer autoincrement not null,
  user_id                       integer,
  name                          varchar(255),
  description                   TEXT,
  creation_date                 timestamp,
  status                        varchar(255),
  constraint pk_scenario primary key (id)
);

create table setting (
  name                          varchar(255) not null,
  value                         varchar(255),
  constraint pk_setting primary key (name)
);

create table user (
  id                            integer autoincrement not null,
  role_id                       integer,
  name                          varchar(255),
  password                      varchar(255),
  constraint pk_user primary key (id)
);

alter table column_csv add constraint fk_column_csv_data_file_id foreign key (data_file_id) references data_file (id) on delete restrict on update restrict;
create index ix_column_csv_data_file_id on column_csv (data_file_id);

alter table data_file add constraint fk_data_file_file_id foreign key (file_id) references file (id) on delete restrict on update restrict;

alter table parameter_file add constraint fk_parameter_file_file_id foreign key (file_id) references file (id) on delete restrict on update restrict;

alter table run add constraint fk_run_scenario_id foreign key (scenario_id) references scenario (id) on delete restrict on update restrict;
create index ix_run_scenario_id on run (scenario_id);

alter table scenario add constraint fk_scenario_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_scenario_user_id on scenario (user_id);

alter table user add constraint fk_user_role_id foreign key (role_id) references role (id) on delete restrict on update restrict;
create index ix_user_role_id on user (role_id);


# --- !Downs

alter table column_csv drop constraint if exists fk_column_csv_data_file_id;
drop index if exists ix_column_csv_data_file_id;

alter table data_file drop constraint if exists fk_data_file_file_id;

alter table parameter_file drop constraint if exists fk_parameter_file_file_id;

alter table run drop constraint if exists fk_run_scenario_id;
drop index if exists ix_run_scenario_id;

alter table scenario drop constraint if exists fk_scenario_user_id;
drop index if exists ix_scenario_user_id;

alter table user drop constraint if exists fk_user_role_id;
drop index if exists ix_user_role_id;

drop table if exists column_csv;

drop table if exists data_file;

drop table if exists file;

drop table if exists parameter_file;

drop table if exists role;

drop table if exists run;

drop table if exists scenario;

drop table if exists setting;

drop table if exists user;

