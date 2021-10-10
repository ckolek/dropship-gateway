CREATE TABLE IF NOT EXISTS catalog
(
    id             BIGSERIAL   NOT NULL,
    supplier_id    BIGINT      NOT NULL,
    external_id    TEXT        NOT NULL,
    description    TEXT        NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT catalog_pk PRIMARY KEY (id),
    CONSTRAINT catalog_uk01 UNIQUE (supplier_id, external_id),
    CONSTRAINT catalog_fk01 FOREIGN KEY (supplier_id) REFERENCES supplier (id)
);

CREATE TABLE IF NOT EXISTS catalog_item
(
    id                BIGSERIAL   NOT NULL,
    catalog_id        BIGINT      NOT NULL,
    parent_item_id    BIGINT      NULL,
    name              TEXT        NULL,
    short_description TEXT        NULL,
    long_description  TEXT        NULL,
    sku               TEXT        NOT NULL,
    gtin              TEXT        NULL,
    upc               TEXT        NULL,
    ean               TEXT        NULL,
    isbn              TEXT        NULL,
    mpn               TEXT        NULL,
    manufacturer      TEXT        NULL,
    brand             TEXT        NULL,
    time_created      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version    SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT catalog_item_pk PRIMARY KEY (id),
    CONSTRAINT catalog_item_uk01 UNIQUE (catalog_id, sku),
    CONSTRAINT catalog_item_fk01 FOREIGN KEY (catalog_id) REFERENCES catalog (id),
    CONSTRAINT catalog_item_fk02 FOREIGN KEY (parent_item_id) REFERENCES catalog_item (id)
);

CREATE INDEX IF NOT EXISTS catalog_item_idx01 ON catalog_item (parent_item_id);
