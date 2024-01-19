create table country (
    id serial primary key,
    name varchar(255) not null unique,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table city (
    id serial primary key,
    name varchar(255) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    country_id bigint REFERENCES country(id) ON DELETE SET DEFAULT
);

create table address (
    id serial primary key,
    street varchar(255) not null,
    latitude double precision,
    longitude double precision,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    country_id bigint REFERENCES country(id) ON DELETE SET DEFAULT,
    city_id bigint REFERENCES city(id) ON DELETE SET DEFAULT
);

create table user_profile (
    id serial primary key,
    name varchar(255) not null,
    surname varchar(255) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    user_id bigint REFERENCES user_entity(id),
    address_id bigint REFERENCES address(id)
);

create table role (
    id serial primary key,
    name varchar(80) not null unique,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table privilege (
    id serial primary key,
    name varchar(80) not null unique,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table role_privilege (
    id serial primary key,
    role_id bigint REFERENCES role(id),
    privilege_id bigint REFERENCES privilege(id)
);

alter table user_entity
add column role_id bigint REFERENCES role(id) ON DELETE SET DEFAULT,
add constraint u_name_unique unique(username, email);