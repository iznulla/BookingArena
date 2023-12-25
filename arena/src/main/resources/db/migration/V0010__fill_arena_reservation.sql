insert into address (street, country_id, city_id, longitude, latitude) values ('AFL Eco park', 1, 1, 41.311323984467414, 69.29473391297348); --1
insert into address (street, country_id, city_id, longitude, latitude) values ('Sharof rashidov', 1, 1, 41.33090414932377, 69.279411615623);--2
insert into address (street, country_id, city_id, longitude, latitude) values ('Футбольная школа Амира', 1, 1, 41.33109332462163, 69.2763069104027);--3
insert into address (street, country_id, city_id, longitude, latitude) values ('Лабзак', 1, 1, 41.32832032191009, 69.26113062713519);--4
insert into address (street, country_id, city_id, longitude, latitude) values ('Бодомзор', 1, 1, 41.34023677799262, 69.29382100910559);--5

insert into address (street, country_id, city_id, longitude, latitude) values ('Dilimah', 1, 2, 39.66293502898839, 66.93413056090466);--6
insert into address (street, country_id, city_id, longitude, latitude) values ('Zilolbaht', 1, 2, 39.64451473443719, 66.95633480032);--7
insert into address (street, country_id, city_id, longitude, latitude) values ('Travmatologiya', 1, 2, 39.67975328370735, 66.91646886815883);--8
insert into address (street, country_id, city_id, longitude, latitude) values ('Yunusabad', 1, 1, 39.66293502898839, 66.93413056090466);--9

insert into users (username, password, email, role_id, is_active) values ('user', 'user', 'user', 1, 'true');
insert into user_profile (name, surname, user_id, address_id) values ('user', 'user', 1, 9);


insert into arena (name, description, status, user_id) values ('Eco Park', 'Best', 'true', 1);--1
insert into arena (name, description, status, user_id) values ('Sharof Rashidov', 'Best', 'true', 1);--2
insert into arena (name, description, status, user_id) values ('ФШ Амора', 'Best', 'true', 1);--3
insert into arena (name, description, status, user_id) values ('Lazbek', 'Best', 'true', 1);--4
insert into arena (name, description, status, user_id) values ('Bodomzor', 'Best', 'true', 1);--5
insert into arena (name, description, status, user_id) values ('Dilimah', 'Best', 'true', 1);--6
insert into arena (name, description, status, user_id) values ('zilolbaht', 'Best', 'true', 1);--7
insert into arena (name, description, status, user_id) values ('travmotologiya', 'Best', 'true', 1);--8

insert into arena_info(phone, price, worked_from, worked_to, address_id, arena_id) values ('+998712345678', 100000, '08:00', '18:00', 1, 1);
insert into arena_info(phone, price, worked_from, worked_to, address_id, arena_id) values ('+998714321234', 120000, '08:00', '18:00', 2, 2);
insert into arena_info(phone, price, worked_from, worked_to, address_id, arena_id) values ('+998712342318', 125000, '08:00', '18:00', 3, 3);
insert into arena_info(phone, price, worked_from, worked_to, address_id, arena_id) values ('+998712534554', 133000, '08:00', '18:00', 4, 4);
insert into arena_info(phone, price, worked_from, worked_to, address_id, arena_id) values ('+998713232235', 105000, '08:00', '18:00', 5, 5);
insert into arena_info(phone, price, worked_from, worked_to, address_id, arena_id) values ('+998778435679', 100000, '08:00', '18:00', 6, 6);
insert into arena_info(phone, price, worked_from, worked_to, address_id, arena_id) values ('+998719674268', 120000, '08:00', '18:00', 7, 7);
insert into arena_info(phone, price, worked_from, worked_to, address_id, arena_id) values ('+998711840367', 125000, '08:00', '18:00', 8, 8);
