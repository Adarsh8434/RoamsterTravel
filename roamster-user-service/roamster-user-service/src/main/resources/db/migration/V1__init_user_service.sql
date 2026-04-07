-- V1__init_user_service.sql
-- Roamster User Service - Initial Schema

CREATE TABLE IF NOT EXISTS users (
    id            BIGSERIAL PRIMARY KEY,
    login         VARCHAR(30)  NOT NULL UNIQUE,
    email         VARCHAR(255) NOT NULL UNIQUE,
    phone         VARCHAR(20)  UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'USER',   -- USER | ADMIN | CREATOR | PARTNER
    activate      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_profiles (
    id           BIGSERIAL PRIMARY KEY,
    phone_number VARCHAR(20),
    age          INTEGER,
    gender       VARCHAR(10),                             -- MALE | FEMALE | OTHER
    city         VARCHAR(100),
    user_id      BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    created_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_preferences (
    id              BIGSERIAL PRIMARY KEY,
    travel_style    VARCHAR(20),                         -- RELAXED | ADVENTURE | AESTHETIC
    budget_range    VARCHAR(20),                         -- BASIC | MODERATE | PREMIUM
    food_preference VARCHAR(20),                         -- VEG | NON_VEG | VEGAN | JAIN
    user_id         BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_login ON users(login);
CREATE INDEX IF NOT EXISTS idx_user_profiles_user_id ON user_profiles(user_id);
CREATE INDEX IF NOT EXISTS idx_user_preferences_user_id ON user_preferences(user_id);
