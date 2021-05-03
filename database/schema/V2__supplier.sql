CREATE TABLE IF NOT EXISTS supplier
(
    id             BIGSERIAL   NOT NULL,
    name           TEXT        NOT NULL,
    status         TEXT        NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT supplier_pk PRIMARY KEY (id),
    CONSTRAINT supplier_uk01 UNIQUE (name)
);

CREATE INDEX IF NOT EXISTS supplier_idx01 ON supplier (status);

ALTER TABLE supplier OWNER TO dsgw;
REVOKE ALL ON TABLE supplier FROM PUBLIC;
GRANT ALL ON TABLE supplier TO dsgw;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE supplier to dsgwapp;

ALTER SEQUENCE supplier_id_seq OWNER TO dsgw;
ALTER SEQUENCE supplier_id_seq RESTART WITH 1000000;
REVOKE ALL ON SEQUENCE supplier_id_seq FROM PUBLIC;
GRANT ALL ON SEQUENCE supplier_id_seq TO dsgw;
GRANT SELECT, UPDATE ON SEQUENCE supplier_id_seq TO dsgwapp;