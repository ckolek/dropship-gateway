CREATE TABLE IF NOT EXISTS order_cancel_code
(
    id             BIGSERIAL   NOT NULL,
    code           TEXT        NOT NULL,
    description    TEXT        NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT order_cancel_code_pk PRIMARY KEY (id),
    CONSTRAINT order_cancel_code_uk01 UNIQUE (code)
);

ALTER TABLE order_cancel_code OWNER TO dsgw;
REVOKE ALL ON TABLE order_cancel_code FROM PUBLIC;
GRANT ALL ON TABLE order_cancel_code TO dsgw;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE order_cancel_code to dsgwapp;

ALTER SEQUENCE order_cancel_code_id_seq OWNER TO dsgw;
ALTER SEQUENCE order_cancel_code_id_seq RESTART WITH 1000000;
REVOKE ALL ON SEQUENCE order_cancel_code_id_seq FROM PUBLIC;
GRANT ALL ON SEQUENCE order_cancel_code_id_seq TO dsgw;
GRANT SELECT, UPDATE ON SEQUENCE order_cancel_code_id_seq TO dsgwapp;

CREATE TABLE IF NOT EXISTS "order"
(
    id                    BIGSERIAL   NOT NULL,
    external_id           TEXT        NOT NULL,
    customer_order_number TEXT        NOT NULL,
    warehouse_id          BIGINT      NOT NULL,
    name                  TEXT        NULL,
    phone                 TEXT        NULL,
    email                 TEXT        NULL,
    line1                 TEXT        NOT NULL,
    line2                 TEXT        NULL,
    line3                 TEXT        NULL,
    city                  TEXT        NOT NULL,
    state                 TEXT        NULL,
    province              TEXT        NULL,
    postal_code           TEXT        NOT NULL,
    country               CHAR(3)     NOT NULL,
    service_level_id      BIGINT      NULL,
    status                TEXT        NOT NULL,
    cancel_code_id        BIGINT      NULL,
    cancel_reason         TEXT        NULL,
    time_ordered          TIMESTAMPTZ NOT NULL,
    time_released         TIMESTAMPTZ NOT NULL,
    time_cancelled        TIMESTAMPTZ NULL,
    time_created          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version        SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT order_pk PRIMARY KEY (id),
    CONSTRAINT order_uk01 UNIQUE (external_id),
    CONSTRAINT order_fk01 FOREIGN KEY (warehouse_id) REFERENCES warehouse (id),
    CONSTRAINT order_fk02 FOREIGN KEY (service_level_id) REFERENCES service_level (id),
    CONSTRAINT order_fk03 FOREIGN KEY (cancel_code_id) REFERENCES order_cancel_code (id)
);

CREATE INDEX IF NOT EXISTS order_idx01 ON "order" (warehouse_id);
CREATE INDEX IF NOT EXISTS order_idx02 ON "order" (service_level_id);
CREATE INDEX IF NOT EXISTS order_idx03 ON "order" (cancel_code_id);

ALTER TABLE "order" OWNER TO dsgw;
REVOKE ALL ON TABLE "order" FROM PUBLIC;
GRANT ALL ON TABLE "order" TO dsgw;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE "order" to dsgwapp;

ALTER SEQUENCE order_id_seq OWNER TO dsgw;
ALTER SEQUENCE order_id_seq RESTART WITH 1000000;
REVOKE ALL ON SEQUENCE order_id_seq FROM PUBLIC;
GRANT ALL ON SEQUENCE order_id_seq TO dsgw;
GRANT SELECT, UPDATE ON SEQUENCE order_id_seq TO dsgwapp;

CREATE TABLE IF NOT EXISTS order_item
(
    id                     BIGSERIAL   NOT NULL,
    order_id               BIGINT      NOT NULL,
    line_number            SMALLINT    NOT NULL,
    catalog_item_id        BIGINT      NOT NULL,
    quantity               SMALLINT    NOT NULL,
    customization          TEXT        NULL,
    expected_ship_date     TIMESTAMPTZ NULL,
    expected_delivery_date TIMESTAMPTZ NULL,
    cancel_code_id         BIGINT      NULL,
    cancel_reason          TEXT        NULL,
    time_cancelled         TIMESTAMPTZ NULL,
    time_created           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version         SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT order_item_pk PRIMARY KEY (id),
    CONSTRAINT order_item_uk02 UNIQUE (order_id, line_number),
    CONSTRAINT order_item_fk01 FOREIGN KEY (order_id) REFERENCES "order" (id) ON DELETE CASCADE,
    CONSTRAINT order_item_fk02 FOREIGN KEY (catalog_item_id) REFERENCES catalog_item (id),
    CONSTRAINT order_item_fk03 FOREIGN KEY (cancel_code_id) REFERENCES order_cancel_code (id)
);

CREATE INDEX IF NOT EXISTS order_item_idx01 ON order_item (catalog_item_id);
CREATE INDEX IF NOT EXISTS order_item_idx02 ON order_item (cancel_code_id);

ALTER TABLE order_item OWNER TO dsgw;
REVOKE ALL ON TABLE order_item FROM PUBLIC;
GRANT ALL ON TABLE order_item TO dsgw;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE order_item to dsgwapp;

ALTER SEQUENCE order_item_id_seq OWNER TO dsgw;
ALTER SEQUENCE order_item_id_seq RESTART WITH 1000000;
REVOKE ALL ON SEQUENCE order_item_id_seq FROM PUBLIC;
GRANT ALL ON SEQUENCE order_item_id_seq TO dsgw;
GRANT SELECT, UPDATE ON SEQUENCE order_item_id_seq TO dsgwapp;