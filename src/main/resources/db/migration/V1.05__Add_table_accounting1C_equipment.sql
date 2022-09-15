create table Accounting1C (
    id bigint,
    inventoryNumber varchar(255),
    title varchar(255),
    employee bigint,
    CONSTRAINT accounting1C_pkey primary key (id)
)
with (
    OIDS=FALSE
    );

create table Equipment (
    id bigint,
    typeEquipment varchar(100) not null,
    manufacturer varchar(255),
    model varchar(255),
    uid varchar(255),
    accounting1С bigint,
    workplace bigint,
    constraint equipment_pkey primary key (id)
)
    with (
        OIDS=FALSE
        );

alter table if exists Accounting1C
    add constraint fk_employee foreign key (employee) references Employee (id)
    ON UPDATE CASCADE ON DELETE SET NULL;

alter table if exists Equipment
    add constraint fk_workplace foreign key (workplace) references Workplace (id)
    ON UPDATE CASCADE ON DELETE SET NULL;

alter table if exists Equipment
    add constraint fk_accounting1C foreign key (accounting1С) references Accounting1C(id)
    ON UPDATE CASCADE ON DELETE SET NULL;

