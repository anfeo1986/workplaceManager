create table VirtualMachine
(
    id       bigint,
    ip       varchar(255),
    typeOS   varchar(255),
    vendor   varchar(255),
    version  varchar(255),
    computer bigint,
    CONSTRAINT vm_pkey primary key (id)
) with (
      OIDS = FALSE
      );

alter table if exists VirtualMachine
    add constraint fk_computer_virtual_machine foreign key (computer) references Equipment(id) on
update cascade
on
delete
cascade;
