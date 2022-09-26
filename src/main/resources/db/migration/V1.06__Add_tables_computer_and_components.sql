alter table if exists Equipment
    add column motherboard bigint,
    add column processor bigint,
    add column videocard bigint;

create table Motherboard (
    id bigint,
    manufacturer varchar(255),
    model varchar (255),
    socket varchar (255),
    typeRam varchar (255),
    ramFrequency varchar (255),
    ramMaxAmount varchar (255),
    CONSTRAINT motherboard_pkey primary key (id)
)
    with (
        OIDS=FALSE
        );

alter table if exists Equipment
    add constraint fk_motherboard foreign key (motherboard) references Motherboard(id) ON UPDATE cascade ON DELETE cascade;

create table Processor (
    id bigint,
    model varchar(255),
    numberOfCores varchar(255),
    frequency varchar (255),
    socket varchar(255),
    typeRam varchar(255),
    ramMaxAmount varchar(255),
    graphicsCore varchar(255),
    CONSTRAINT processor_pkey primary key (id)
)
with (
     OIDS=FALSE
);

alter table if exists Equipment
    add constraint fk_processor foreign key (processor) references Processor(id) on update cascade on delete cascade;

create table Ram (
    id bigint,
    model varchar (255),
    typeRam varchar (255),
    amount varchar (255),
    frequency varchar (255),
    computer bigint,
    CONSTRAINT ram_pkey primary key (id)
) with (
    OIDS = FALSE
);

alter table if exists Ram
    add constraint fk_computer foreign key (computer) references Equipment(id) on update cascade on delete cascade;

create table Videocard(
    id bigint,
    manufacturer varchar (255),
    model varchar (255),
    CONSTRAINT videocard_pkey primary key (id)
) with (OIDS=FALSE);

alter table if exists Equipment
    add constraint fk_videocard foreign key (videocard) references Videocard(id) on update cascade on delete cascade;

create table HardDrive (
    id bigint,
    manufacturer varchar (255),
    model varchar (255),
    amount varchar (255),
    typeHardDrive varchar (255),
    computer bigint,
    CONSTRAINT hard_drive_pkey primary key (id)
) with (OIDS=FALSE);

alter table if exists HardDrive
    add constraint fk_computer foreign key (computer) references Equipment(id) on update cascade on delete cascade;


