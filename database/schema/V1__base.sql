DO $DO$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_roles WHERE rolname = 'dsgwapp') THEN
            CREATE ROLE dsgwapp;
        END IF;
    END;
$DO$;

REVOKE ALL ON SCHEMA public FROM PUBLIC;
GRANT USAGE ON SCHEMA public TO dsgwapp;