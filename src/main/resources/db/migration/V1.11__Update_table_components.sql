alter table Motherboard drop column socket, drop column typeRam, drop column ramFrequency, drop column ramMaxAmount;

alter table Processor add column computer bigint;
alter table Processor add constraint fk_computer_processor foreign key (computer) references Equipment(id)
    on update cascade on delete cascade;
alter table Processor drop column typeRam, drop column ramMaxAmount, drop column graphicsCore;

alter table Ram add column deviceLocator varchar (255);

alter table HardDrive drop column manufacturer, drop column amount, drop column typeHardDrive;

alter table Videocard drop column manufacturer;
alter table Videocard add column computer bigint;
alter table Videocard add constraint fk_computer_videocard foreign key (computer) references Equipment(id)
    on update cascade on delete cascade;

alter table OperationSystem add column netName varchar (255);

alter table Equipment drop constraint fk_processor;
alter table Equipment drop constraint fk_videocard;
alter table Equipment drop column processor, drop column videocard;





