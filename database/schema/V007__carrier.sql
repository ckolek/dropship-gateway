CREATE TABLE IF NOT EXISTS carrier
(
    id             BIGSERIAL   NOT NULL,
    name           TEXT        NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT carrier_pk PRIMARY KEY (id),
    CONSTRAINT carrier_uk01 UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS service_level
(
    id             BIGSERIAL   NOT NULL,
    carrier_id     BIGINT      NOT NULL,
    mode           TEXT        NOT NULL,
    code           TEXT        NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT service_level_pk PRIMARY KEY (id),
    CONSTRAINT service_level_uk01 UNIQUE (carrier_id, mode),
    CONSTRAINT service_level_uk02 UNIQUE (code),
    CONSTRAINT service_level_fk01 FOREIGN KEY (carrier_id) references carrier (id)
);
