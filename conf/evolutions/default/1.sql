# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table role (
  id                            integer autoincrement not null,
  name                          varchar(255),
  constraint pk_role primary key (id)
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

alter table user add constraint fk_user_role_id foreign key (role_id) references role (id) on delete restrict on update restrict;
create index ix_user_role_id on user (role_id);


# --- !Downs

alter table user drop constraint if exists fk_user_role_id;
drop index if exists ix_user_role_id;

drop table if exists role;

drop table if exists setting;

drop table if exists user;

