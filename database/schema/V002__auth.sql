CREATE TABLE IF NOT EXISTS client_credentials
(
    id             BIGSERIAL   NOT NULL,
    client_id      TEXT        NOT NULL,
    client_secret  BYTEA       NOT NULL,
    client_type    TEXT        NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT client_credentials_pk PRIMARY KEY (id),
    CONSTRAINT client_credentials_uk01 UNIQUE (client_id)
);

CREATE TABLE IF NOT EXISTS client_service
(
    id             BIGINT      NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT client_service_pk PRIMARY KEY (id),
    CONSTRAINT client_service_fk01 FOREIGN KEY (id) REFERENCES client_credentials (id)
);

CREATE TABLE IF NOT EXISTS client_organization
(
    id             BIGINT      NOT NULL,
    name           TEXT        NOT NULL,
    type           TEXT        NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT client_organization_pk PRIMARY KEY (id),
    CONSTRAINT client_organization_uk01 UNIQUE (name),
    CONSTRAINT client_organization_fk01 FOREIGN KEY (id) REFERENCES client_credentials (id)
);

CREATE TABLE IF NOT EXISTS client_user
(
    id              BIGINT      NOT NULL,
    organization_id BIGINT      NOT NULL,
    name            TEXT        NOT NULL,
    time_created    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version  SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT client_user_pk PRIMARY KEY (id),
    CONSTRAINT client_user_uk01 UNIQUE (name),
    CONSTRAINT client_user_fk01 FOREIGN KEY (id) REFERENCES client_credentials (id),
    CONSTRAINT client_user_fk02 FOREIGN KEY (organization_id) REFERENCES client_organization (id)
);

CREATE INDEX IF NOT EXISTS client_user_idx01 ON client_user (organization_id);

CREATE TABLE IF NOT EXISTS client_role
(
    id             BIGSERIAL   NOT NULL,
    owner_id       BIGINT      NOT NULL,
    name           TEXT        NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT client_role_pk PRIMARY KEY (id),
    CONSTRAINT client_role_uk01 UNIQUE (owner_id, name),
    CONSTRAINT client_role_fk01 FOREIGN KEY (owner_id) REFERENCES client_organization (id)
);

CREATE TABLE IF NOT EXISTS client_roles
(
    client_id BIGINT NOT NULL,
    role_id   BIGINT NOT NULL,
    CONSTRAINT client_roles_uk01 UNIQUE (client_id, role_id),
    CONSTRAINT client_roles_fk01 FOREIGN KEY (client_id) REFERENCES client_credentials (id),
    CONSTRAINT client_roles_fk02 FOREIGN KEY (role_id) REFERENCES client_role (id)
);

CREATE TABLE IF NOT EXISTS client_scope
(
    id             BIGSERIAL   NOT NULL,
    value          TEXT        NOT NULL,
    time_created   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    time_updated   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    record_version SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT client_scope_pk PRIMARY KEY (id),
    CONSTRAINT client_scope_uk01 UNIQUE (value)
);

CREATE TABLE IF NOT EXISTS client_role_scopes
(
    role_id  BIGINT NOT NULL,
    scope_id BIGINT NOT NULL,
    CONSTRAINT client_role_scopes_uk01 UNIQUE (role_id, scope_id),
    CONSTRAINT client_role_scopes_fk01 FOREIGN KEY (role_id) REFERENCES client_role (id),
    CONSTRAINT client_role_scopes_fk02 FOREIGN KEY (scope_id) REFERENCES client_scope (id)
);
