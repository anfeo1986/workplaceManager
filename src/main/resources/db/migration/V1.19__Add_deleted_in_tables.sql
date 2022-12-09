alter table Workplace add column deleted boolean;
update Workplace set deleted='f';
alter table Workplace alter column deleted set not null;
alter table Workplace alter column deleted set default false;

alter table Users add column deleted boolean;
update Users set deleted='f';
alter table Users alter column deleted set not null;
alter table Users alter column deleted set default false;

alter table Equipment add column deleted boolean;
update Equipment set deleted='f';
alter table Equipment alter column deleted set not null;
alter table Equipment alter column deleted set default false;

alter table Employee add column deleted boolean;
update Employee set deleted='f';
alter table Employee alter column deleted set not null;
alter table Employee alter column deleted set default false;

alter table Accounting1C add column deleted boolean;
update Accounting1C set deleted='f';
alter table Accounting1C alter column deleted set not null;
alter table Accounting1C alter column deleted set default false;




