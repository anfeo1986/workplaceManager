CREATE SEQUENCE IF NOT EXISTS public.hibernate_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.hibernate_sequence
    OWNER TO admin;

CREATE OR REPLACE FUNCTION public.newid(
	)
    RETURNS bigint
    LANGUAGE 'sql'
    COST 100
    VOLATILE STRICT PARALLEL UNSAFE
AS $BODY$
SELECT nextval('hibernate_sequence')
           $BODY$;

ALTER FUNCTION public.newid()
    OWNER TO admin;
