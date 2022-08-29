create table films (
    id  serial not null,
    genre varchar(255),
    title varchar(255),
    watched boolean,
    year int4,
    primary key (id))