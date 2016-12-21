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

insert into setting values ("projectName", "ROShiny")

create table user (
  id                            integer not null,
  role_id                       integer,
  name                          varchar(255),
  password                      varchar(255),
  constraint pk_user primary key (id)
);

insert into user values (1, 1, "admin", "");
