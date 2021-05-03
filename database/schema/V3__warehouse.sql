CREATE TABLE IF NOT EXISTS warehouse
(
    id             BIGSERIAL   NOT NULL,
    supplier_id    BIGINT      NOT NULL,
    code           TEXT        NOT NULL,
    supplier_code  TEXT        NOT NULL,
    description    TEXT        NULL,
    status         TEXT        NOT NULL,
    line1          TEXT        NOT NULL,
    line2          TEXT        NULL,
    line3          TEXT        NULL,
    city           TEXT        NOT NULL,
    state          TEXT        NULL,
    province       TEXT        NULL,
    postal_code    TEXT        NOT NULL,
    country        CHAR(3)     NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT warehouse_pk PRIMARY KEY (id),
    CONSTRAINT warehouse_uk01 UNIQUE (code),
    CONSTRAINT warehouse_uk02 UNIQUE (supplier_id, supplier_code),
    CONSTRAINT warehouse_fk01 FOREIGN KEY (supplier_id) REFERENCES supplier (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS warehouse_idx01 ON warehouse (status);

ALTER TABLE warehouse OWNER TO dsgw;
REVOKE ALL ON TABLE warehouse FROM PUBLIC;
GRANT ALL ON TABLE warehouse TO dsgw;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE warehouse to dsgwapp;

ALTER SEQUENCE warehouse_id_seq OWNER TO dsgw;
ALTER SEQUENCE warehouse_id_seq RESTART WITH 1000000;
REVOKE ALL ON SEQUENCE warehouse_id_seq FROM PUBLIC;
GRANT ALL ON SEQUENCE warehouse_id_seq TO dsgw;
GRANT SELECT, UPDATE ON SEQUENCE warehouse_id_seq TO dsgwapp;

CREATE TABLE IF NOT EXISTS warehouse_oper_day
(
    id             BIGSERIAL   NOT NULL,
    warehouse_id   BIGINT      NOT NULL,
    day_of_week    SMALLINT    NOT NULL,
    open           BOOLEAN     NOT NULL,
    open_time      TIME        NULL,
    close_time     TIME        NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT now(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT now(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT warehouse_oper_day_pk PRIMARY KEY (id),
    CONSTRAINT warehouse_oper_day_uk01 UNIQUE (warehouse_id, day_of_week),
    CONSTRAINT warehouse_oper_day_fk01 FOREIGN KEY (warehouse_id) REFERENCES warehouse (id) ON DELETE CASCADE,
    CONSTRAINT warehouse_oper_day_ck01 CHECK (NOT open OR (open_time IS NOT NULL AND close_time IS NOT NULL))
);

ALTER TABLE warehouse_oper_day OWNER TO dsgw;
REVOKE ALL ON TABLE warehouse_oper_day FROM PUBLIC;
GRANT ALL ON TABLE warehouse_oper_day TO dsgw;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE warehouse_oper_day to dsgwapp;

ALTER SEQUENCE warehouse_oper_day_id_seq OWNER TO dsgw;
ALTER SEQUENCE warehouse_oper_day_id_seq RESTART WITH 1000000;
REVOKE ALL ON SEQUENCE warehouse_oper_day_id_seq FROM PUBLIC;
GRANT ALL ON SEQUENCE warehouse_oper_day_id_seq TO dsgw;
GRANT SELECT, UPDATE ON SEQUENCE warehouse_oper_day_id_seq TO dsgwapp;

CREATE TABLE IF NOT EXISTS warehouse_oper_day_except
(
    id             BIGSERIAL   NOT NULL,
    warehouse_id   BIGINT      NOT NULL,
    date           DATE        NOT NULL,
    open           BOOLEAN     NOT NULL,
    open_time      TIME        NULL,
    close_time     TIME        NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT now(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT now(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT warehouse_oper_day_except_pk PRIMARY KEY (id),
    CONSTRAINT warehouse_oper_day_except_uk01 UNIQUE (warehouse_id, date),
    CONSTRAINT warehouse_oper_day_except_fk01 FOREIGN KEY (warehouse_id) REFERENCES warehouse (id) ON DELETE CASCADE,
    CONSTRAINT warehouse_oper_day_except_ck01 CHECK (NOT open OR (open_time IS NOT NULL AND close_time IS NOT NULL))
);

ALTER TABLE warehouse_oper_day_except OWNER TO dsgw;
REVOKE ALL ON TABLE warehouse_oper_day_except FROM PUBLIC;
GRANT ALL ON TABLE warehouse_oper_day_except TO dsgw;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE warehouse_oper_day_except to dsgwapp;

ALTER SEQUENCE warehouse_oper_day_except_id_seq OWNER TO dsgw;
ALTER SEQUENCE warehouse_oper_day_except_id_seq RESTART WITH 1000000;
REVOKE ALL ON SEQUENCE warehouse_oper_day_except_id_seq FROM PUBLIC;
GRANT ALL ON SEQUENCE warehouse_oper_day_except_id_seq TO dsgw;
GRANT SELECT, UPDATE ON SEQUENCE warehouse_oper_day_except_id_seq TO dsgwapp;