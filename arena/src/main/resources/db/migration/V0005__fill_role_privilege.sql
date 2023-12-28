-- create role
insert into role (name) values ('ADMIN');
insert into role (name) values ('USER');
insert into role (name) values ('MANAGER');

-- create privilege
insert into privilege (name) values ('READ');--1
insert into privilege (name) values ('CREATE');--2
insert into privilege (name) values ('UPDATE');--3
insert into privilege (name) values ('DELETE');--3

insert into privilege (name) values ('CREATE_USER');--4
insert into privilege (name) values ('READ_USER');--5
insert into privilege (name) values ('UPDATE_USER');--6
insert into privilege (name) values ('DELETE_USER');--7

insert into privilege (name) values ('CREATE_ARENA');--8
insert into privilege (name) values ('UPDATE_ARENA');--9
insert into privilege (name) values ('DELETE_ARENA');--10

insert into privilege (name) values ('CREATE_RESERVATION');--11
insert into privilege (name) values ('UPDATE_RESERVATION');--12
insert into privilege (name) values ('DELETE_RESERVATION');--13