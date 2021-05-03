CREATE TABLE IF NOT EXISTS catalog
(
    id             BIGSERIAL   NOT NULL,
    supplier_id    BIGINT      NOT NULL,
    external_id    TEXT        NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT catalog_pk PRIMARY KEY (id),
    CONSTRAINT catalog_uk01 UNIQUE (supplier_id, external_id),
    CONSTRAINT catalog_fk01 FOREIGN KEY (supplier_id) REFERENCES supplier (id)
);

ALTER TABLE catalog OWNER TO dsgw;
REVOKE ALL ON TABLE catalog FROM PUBLIC;
GRANT ALL ON TABLE catalog TO dsgw;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE catalog to dsgwapp;

ALTER SEQUENCE catalog_id_seq OWNER TO dsgw;
ALTER SEQUENCE catalog_id_seq RESTART WITH 1000000;
REVOKE ALL ON SEQUENCE catalog_id_seq FROM PUBLIC;
GRANT ALL ON SEQUENCE catalog_id_seq TO dsgw;
GRANT SELECT, UPDATE ON SEQUENCE catalog_id_seq TO dsgwapp;

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

ALTER TABLE catalog_item OWNER TO dsgw;
REVOKE ALL ON TABLE catalog_item FROM PUBLIC;
GRANT ALL ON TABLE catalog_item TO dsgw;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE catalog_item to dsgwapp;

ALTER SEQUENCE catalog_item_id_seq OWNER TO dsgw;
ALTER SEQUENCE catalog_item_id_seq RESTART WITH 1000000;
REVOKE ALL ON SEQUENCE catalog_item_id_seq FROM PUBLIC;
GRANT ALL ON SEQUENCE catalog_item_id_seq TO dsgw;
GRANT SELECT, UPDATE ON SEQUENCE catalog_item_id_seq TO dsgwapp;