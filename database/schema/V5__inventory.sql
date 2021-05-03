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

ALTER TABLE item_inventory OWNER TO dsgw;
REVOKE ALL ON TABLE item_inventory FROM PUBLIC;
GRANT ALL ON TABLE item_inventory TO dsgw;
GRANT INSERT, SELECT, UPDATE, DELETE ON TABLE item_inventory to dsgwapp;

ALTER SEQUENCE item_inventory_id_seq OWNER TO dsgw;
ALTER SEQUENCE item_inventory_id_seq RESTART WITH 1000000;
REVOKE ALL ON SEQUENCE item_inventory_id_seq FROM PUBLIC;
GRANT ALL ON SEQUENCE item_inventory_id_seq TO dsgw;
GRANT SELECT, UPDATE ON SEQUENCE item_inventory_id_seq TO dsgwapp;