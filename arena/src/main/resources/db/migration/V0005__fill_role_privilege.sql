-- create role

insert into role (name) values ('USER');

-- create privilege
insert into privilege (name) values ('READ');
insert into privilege (name) values ('CREATE');
insert into privilege (name) values ('UPDATE');
insert into privilege (name) values ('DELETE');