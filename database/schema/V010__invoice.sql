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
