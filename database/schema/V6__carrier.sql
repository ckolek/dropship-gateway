CREATE TABLE IF NOT EXISTS carrier
(
    id             BIGSERIAL   NOT NULL,
    name           TEXT        NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT carrier_pk PRIMARY KEY (id),
    CONSTRAINT carrier_uk01 UNIQUE (name)
);

ALTER TABLE carrier OWNER TO dsgw;
REVOKE ALL ON TABLE carrier FROM PUBLIC;
GRANT ALL ON TABLE carrier TO dsgw;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE carrier to dsgwapp;

ALTER SEQUENCE carrier_id_seq OWNER TO dsgw;
ALTER SEQUENCE carrier_id_seq RESTART WITH 1000000;
REVOKE ALL ON SEQUENCE carrier_id_seq FROM PUBLIC;
GRANT ALL ON SEQUENCE carrier_id_seq TO dsgw;
GRANT SELECT, UPDATE ON SEQUENCE carrier_id_seq TO dsgwapp;

CREATE TABLE IF NOT EXISTS service_level
(
    id             BIGSERIAL   NOT NULL,
    carrier_id     BIGINT      NOT NULL,
    mode           TEXT        NOT NULL,
    code           TEXT        NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT service_level_pk PRIMARY KEY (id),
    CONSTRAINT service_level_uk01 UNIQUE (carrier_id, mode),
    CONSTRAINT service_level_uk02 UNIQUE (code),
    CONSTRAINT service_level_fk01 FOREIGN KEY (carrier_id) references carrier (id)
);

ALTER TABLE service_level OWNER TO dsgw;
REVOKE ALL ON TABLE service_level FROM PUBLIC;
GRANT ALL ON TABLE service_level TO dsgw;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE service_level to dsgwapp;

ALTER SEQUENCE service_level_id_seq OWNER TO dsgw;
ALTER SEQUENCE service_level_id_seq RESTART WITH 1000000;
REVOKE ALL ON SEQUENCE service_level_id_seq FROM PUBLIC;
GRANT ALL ON SEQUENCE service_level_id_seq TO dsgw;
GRANT SELECT, UPDATE ON SEQUENCE service_level_id_seq TO dsgwapp;