CREATE TABLE IF NOT EXISTS supplier
(
    id             BIGSERIAL   NOT NULL,
    name           TEXT        NOT NULL,
    status         TEXT        NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT supplier_pk PRIMARY KEY (id),
    CONSTRAINT supplier_uk01 UNIQUE (name)
);

CREATE INDEX IF NOT EXISTS supplier_idx01 ON supplier (status);
