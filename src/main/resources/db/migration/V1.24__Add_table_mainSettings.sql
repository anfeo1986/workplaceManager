create table MainSettings
(
    id         bigint not null,
    publicKey  varchar(5000),
    privateKey varchar(5000)
)
    with (
        OIDS = FALSE
        );