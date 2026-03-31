-- ============================================================
-- Roamster :: Food Service
-- Migration: V1__init_food_schema.sql
-- ============================================================

CREATE TABLE IF NOT EXISTS food_recommendation_detail (
    id                  BIGSERIAL       PRIMARY KEY,
    recommendation_id   BIGINT          NOT NULL,
    trip_id             BIGINT          NOT NULL,
    food_type           VARCHAR(20)     CHECK (food_type IN ('VEG','NON_VEG','VEGAN','JAIN')),
    cuisine_type        VARCHAR(50)     CHECK (cuisine_type IN ('LOCAL','CONTINENTAL','STREET','TRADITIONAL')),
    spice_level         VARCHAR(20)     CHECK (spice_level IN ('LOW','MEDIUM','HIGH')),
    suitable_for        VARCHAR(50)     CHECK (suitable_for IN ('SOLO','COUPLE','FAMILY','KIDS','ELDERLY','ALL')),
    best_time           VARCHAR(20)     CHECK (best_time IN ('MORNING','AFTERNOON','EVENING','NIGHT')),
    hygiene_level       VARCHAR(20)     CHECK (hygiene_level IN ('LOW','MEDIUM','HIGH')),
    allergy_warning     TEXT,
    dish_names          VARCHAR(500),
    image_reference_url VARCHAR(500),
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_food_rec_trip      ON food_recommendation_detail(trip_id);
CREATE INDEX idx_food_rec_food_type ON food_recommendation_detail(food_type);
CREATE INDEX idx_food_rec_best_time ON food_recommendation_detail(best_time);
