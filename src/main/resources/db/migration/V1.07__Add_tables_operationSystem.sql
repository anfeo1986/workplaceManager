alter table if exists Equipment
    add column operationSystem bigint;

create table OperationSystem (
    id bigint,
    typeOS varchar (255),
    vendor varchar (255),
    version varchar(255),
    CONSTRAINT operationSystem_pkey primary key (id)
)
    with (
        OIDS=FALSE
        );