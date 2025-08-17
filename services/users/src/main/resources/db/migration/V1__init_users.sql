CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users
(
    id            UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    password_hash VARCHAR(320) NOT NULL,
    salt          VARCHAR(32)  NOT NULL,
    name          VARCHAR(255) NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
    );

CREATE INDEX IF NOT EXISTS idx_users_name ON users (name);

ALTER TABLE users
    ADD CONSTRAINT uk_users_name
        UNIQUE (name);
