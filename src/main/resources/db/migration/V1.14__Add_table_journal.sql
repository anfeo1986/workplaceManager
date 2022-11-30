create table Journal (
    id bigint not null ,
    event varchar(1000),
    typeEvent varchar(255),
    time timestamp,
    typeObject varchar(255),
    idObject bigint
)
    with (
        OIDS=FALSE
        );