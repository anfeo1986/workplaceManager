CREATE TABLE employee (
    id bigint,
    name VARCHAR(255),
    post VARCHAR (255),
    CONSTRAINT employee_pkey PRIMARY KEY (id)
)
WITH (
    OIDS=FALSE
    );
