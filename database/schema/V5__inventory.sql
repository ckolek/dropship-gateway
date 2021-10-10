CREATE TABLE IF NOT EXISTS item_inventory
(
    id                 BIGSERIAL   NOT NULL,
    catalog_item_id    BIGINT      NOT NULL,
    warehouse_id       BIGINT      NOT NULL,
    quantity_available INT         NOT NULL,
    time_created       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version     SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT item_inventory_pk PRIMARY KEY (id),
    CONSTRAINT item_inventory_uk01 UNIQUE (catalog_item_id, warehouse_id),
    CONSTRAINT item_inventory_fk01 FOREIGN KEY (catalog_item_id) REFERENCES catalog_item (id),
    CONSTRAINT item_inventory_fk02 FOREIGN KEY (warehouse_id) REFERENCES warehouse (id)
);
