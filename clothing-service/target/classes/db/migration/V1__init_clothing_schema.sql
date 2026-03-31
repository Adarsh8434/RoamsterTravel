-- ============================================================
-- Roamster :: Clothing Service
-- Migration: V1__init_clothing_schema.sql
-- ============================================================

-- Base recommendations table (shared concept, owned locally by clothing-service)
CREATE TABLE IF NOT EXISTS recommendations (
    id                  BIGSERIAL       PRIMARY KEY,
    category            VARCHAR(50)     NOT NULL DEFAULT 'CLOTHING',
    target_entity_type  VARCHAR(50),
    target_entity_id    BIGINT,
    confidence_score    DECIMAL(5,2),
    reason              TEXT,
    is_safe             BOOLEAN         DEFAULT TRUE,
    generation_source   VARCHAR(20)     CHECK (generation_source IN ('ML','RULE','MANUAL')),
    trip_id             BIGINT          NOT NULL,
    valid_until         TIMESTAMP,
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_rec_trip_id   ON recommendations(trip_id);
CREATE INDEX idx_rec_category  ON recommendations(category);

-- ────────────────────────────────────────────────────────────
-- Clothing-specific recommendation detail
-- ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS clothing_recommendation_detail (
    id                  BIGSERIAL       PRIMARY KEY,
    recommendation_id   BIGINT          NOT NULL REFERENCES recommendations(id) ON DELETE CASCADE,
    clothing_type       VARCHAR(50)     CHECK (clothing_type IN ('CASUAL','FORMAL','TRADITIONAL','SPORTS')),
    fabric_type         VARCHAR(50)     CHECK (fabric_type   IN ('COTTON','LINEN','WOOL','SYNTHETIC')),
    weather_suitability VARCHAR(20)     CHECK (weather_suitability IN ('HOT','COLD','RAINY','ALL')),
    comfort_level       VARCHAR(20)     CHECK (comfort_level IN ('LOW','MEDIUM','HIGH')),
    footwear_suggestion VARCHAR(100),
    safety_note         TEXT,
    color_combination   VARCHAR(100),
    accessories         VARCHAR(200),
    image_reference_url VARCHAR(500),
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_clothing_rec UNIQUE (recommendation_id)
);

-- ────────────────────────────────────────────────────────────
-- Clothing Orders (Rent / Buy)
-- ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS clothing_order (
    id                  BIGSERIAL       PRIMARY KEY,
    user_id             BIGINT          NOT NULL,
    trip_id             BIGINT          NOT NULL,
    merchant_id         BIGINT          NOT NULL,
    recommendation_id   BIGINT          REFERENCES recommendations(id),
    order_type          VARCHAR(10)     NOT NULL CHECK (order_type IN ('RENT','BUY')),
    status              VARCHAR(20)     NOT NULL DEFAULT 'PENDING'
                            CHECK (status IN ('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED','RETURNED')),
    total_amount        DECIMAL(10,2)   NOT NULL,
    delivery_address_id BIGINT,
    payment_id          BIGINT,
    rental_start_date   TIMESTAMP,
    rental_end_date     TIMESTAMP,
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_rental_dates CHECK (
        order_type = 'BUY'
        OR (rental_start_date IS NOT NULL AND rental_end_date IS NOT NULL AND rental_start_date < rental_end_date)
    )
);

CREATE INDEX idx_clothing_order_user    ON clothing_order(user_id);
CREATE INDEX idx_clothing_order_trip    ON clothing_order(trip_id);
CREATE INDEX idx_clothing_order_status  ON clothing_order(status);
CREATE INDEX idx_clothing_order_merchant ON clothing_order(merchant_id);
