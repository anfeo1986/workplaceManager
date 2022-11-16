create table Users (
    id bigint not null ,
    username varchar(255),
    password varchar(255),
    role varchar (255)
)
    with (
        OIDS=FALSE
        );