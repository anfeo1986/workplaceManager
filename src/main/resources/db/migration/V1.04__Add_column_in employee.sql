ALTER TABLE employee ADD workplace bigint;

ALTER TABLE employee ADD CONSTRAINT fk_workplace FOREIGN KEY (workplace)
    REFERENCES workplace (id) ON UPDATE CASCADE ON DELETE SET NULL;
