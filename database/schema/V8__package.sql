CREATE TABLE IF NOT EXISTS package
(
    id                    BIGSERIAL   NOT NULL,
    external_id           TEXT        NOT NULL,
    order_id              BIGINT      NOT NULL,
    warehouse_id          BIGINT      NOT NULL,
    sender_name           TEXT        NULL,
    sender_phone          TEXT        NULL,
    sender_email          TEXT        NULL,
    sender_line1          TEXT        NOT NULL,
    sender_line2          TEXT        NULL,
    sender_line3          TEXT        NULL,
    sender_city           TEXT        NOT NULL,
    sender_state          TEXT        NULL,
    sender_province       TEXT        NULL,
    sender_postal_code    TEXT        NOT NULL,
    sender_country        CHAR(3)     NOT NULL,
    recipient_name        TEXT        NULL,
    recipient_phone       TEXT        NULL,
    recipient_email       TEXT        NULL,
    recipient_line1       TEXT        NOT NULL,
    recipient_line2       TEXT        NULL,
    recipient_line3       TEXT        NULL,
    recipient_city        TEXT        NOT NULL,
    recipient_state       TEXT        NULL,
    recipient_province    TEXT        NULL,
    recipient_postal_code TEXT        NOT NULL,
    recipient_country     CHAR(3)     NOT NULL,
    tracking_number       TEXT        NOT NULL,
    time_shipped          TIMESTAMPTZ NOT NULL,
    time_created          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version        SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT package_pk PRIMARY KEY (id),
    CONSTRAINT package_uk01 UNIQUE (warehouse_id, external_id),
    CONSTRAINT package_fk01 FOREIGN KEY (order_id) REFERENCES "order" (id),
    CONSTRAINT package_fk02 FOREIGN KEY (warehouse_id) REFERENCES warehouse (id)
);

CREATE INDEX IF NOT EXISTS package_idx01 ON package (order_id);

ALTER TABLE package OWNER TO dsgw;
REVOKE ALL ON TABLE package FROM PUBLIC;
GRANT ALL ON TABLE package TO dsgw;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE package to dsgwapp;

ALTER SEQUENCE package_id_seq OWNER TO dsgw;
ALTER SEQUENCE package_id_seq RESTART WITH 1000000;
REVOKE ALL ON SEQUENCE package_id_seq FROM PUBLIC;
GRANT ALL ON SEQUENCE package_id_seq TO dsgw;
GRANT SELECT, UPDATE ON SEQUENCE package_id_seq TO dsgwapp;

CREATE TABLE IF NOT EXISTS package_item
(
    id             BIGSERIAL   NOT NULL,
    package_id     BIGINT      NOT NULL,
    order_item_id  BIGINT      NOT NULL,
    quantity       SMALLINT    NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT package_item_pk PRIMARY KEY (id),
    CONSTRAINT package_item_uk01 UNIQUE (package_id, order_item_id),
    CONSTRAINT package_item_fk01 FOREIGN KEY (package_id) REFERENCES package (id) ON DELETE CASCADE,
    CONSTRAINT package_item_fk02 FOREIGN KEY (order_item_id) REFERENCES order_item (id)
);

ALTER TABLE package_item OWNER TO dsgw;
REVOKE ALL ON TABLE package_item FROM PUBLIC;
GRANT ALL ON TABLE package_item TO dsgw;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE package_item to dsgwapp;

ALTER SEQUENCE package_item_id_seq OWNER TO dsgw;
ALTER SEQUENCE package_item_id_seq RESTART WITH 1000000;
REVOKE ALL ON SEQUENCE package_item_id_seq FROM PUBLIC;
GRANT ALL ON SEQUENCE package_item_id_seq TO dsgw;
GRANT SELECT, UPDATE ON SEQUENCE package_item_id_seq TO dsgwapp;