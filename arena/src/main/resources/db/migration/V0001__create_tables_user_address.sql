create table user_entity (
    id serial primary key,
    username varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    is_active boolean not null default false
);
