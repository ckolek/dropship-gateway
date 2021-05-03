CREATE TABLE IF NOT EXISTS invoice
(
    id             BIGSERIAL   NOT NULL,
    external_id    TEXT        NOT NULL,
    order_id       BIGINT      NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT invoice_pk PRIMARY KEY (id),
    CONSTRAINT invoice_fk01 FOREIGN KEY (order_id) REFERENCES "order" (id)
);

CREATE INDEX IF NOT EXISTS invoice_idx01 ON invoice (order_id);

ALTER TABLE invoice OWNER TO dsgw;
REVOKE ALL ON TABLE invoice FROM PUBLIC;
GRANT ALL ON TABLE invoice TO dsgw;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE invoice to dsgwapp;

ALTER SEQUENCE invoice_id_seq OWNER TO dsgw;
ALTER SEQUENCE invoice_id_seq RESTART WITH 1000000;
REVOKE ALL ON SEQUENCE invoice_id_seq FROM PUBLIC;
GRANT ALL ON SEQUENCE invoice_id_seq TO dsgw;
GRANT SELECT, UPDATE ON SEQUENCE invoice_id_seq TO dsgwapp;

CREATE TABLE IF NOT EXISTS invoice_item
(
    id             BIGSERIAL   NOT NULL,
    invoice_id     BIGINT      NOT NULL,
    order_item_id  BIGINT      NOT NULL,
    quantity       SMALLINT    NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT invoice_item_pk PRIMARY KEY (id),
    CONSTRAINT invoice_item_uk01 UNIQUE (invoice_id, order_item_id),
    CONSTRAINT invoice_item_fk01 FOREIGN KEY (invoice_id) REFERENCES invoice (id) ON DELETE CASCADE,
    CONSTRAINT invoice_item_fk02 FOREIGN KEY (order_item_id) REFERENCES order_item (id)
);

ALTER TABLE invoice_item OWNER TO dsgw;
REVOKE ALL ON TABLE invoice_item FROM PUBLIC;
GRANT ALL ON TABLE invoice_item TO dsgw;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE invoice_item to dsgwapp;

ALTER SEQUENCE invoice_item_id_seq OWNER TO dsgw;
ALTER SEQUENCE invoice_item_id_seq RESTART WITH 1000000;
REVOKE ALL ON SEQUENCE invoice_item_id_seq FROM PUBLIC;
GRANT ALL ON SEQUENCE invoice_item_id_seq TO dsgw;
GRANT SELECT, UPDATE ON SEQUENCE invoice_item_id_seq TO dsgwapp;