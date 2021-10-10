CREATE TABLE IF NOT EXISTS supplier
(
    id              BIGSERIAL   NOT NULL,
    name            TEXT        NOT NULL,
    organization_id BIGINT      NULL,
    status          TEXT        NOT NULL,
    time_created    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version  SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT supplier_pk PRIMARY KEY (id),
    CONSTRAINT supplier_uk01 UNIQUE (name),
    CONSTRAINT supplier_fk01 FOREIGN KEY (organization_id) REFERENCES client_organization (id)
);

CREATE INDEX IF NOT EXISTS supplier_idx01 ON supplier (status);
CREATE UNIQUE INDEX IF NOT EXISTS supplier_idx02 ON supplier (organization_id)
    WHERE organization_id IS NOT NULL;
