-- ============================================================
-- Roamster :: E-commerce / Booking Service
-- Migration: V1__init_ecommerce_schema.sql
-- ============================================================

-- ────────────────────────────────────────────────────────────
-- Merchant
-- ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS merchant (
    id              BIGSERIAL       PRIMARY KEY,
    name            VARCHAR(200)    NOT NULL,
    type            VARCHAR(50)     CHECK (type IN ('CLOTHING','FOOD','MULTI')),
    contact_number  VARCHAR(20),
    city            VARCHAR(100),
    rating          DECIMAL(3,2),
    is_active       BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_merchant_city      ON merchant(city);
CREATE INDEX idx_merchant_type      ON merchant(type);
CREATE INDEX idx_merchant_active    ON merchant(is_active);

-- ────────────────────────────────────────────────────────────
-- Central Orders table
-- ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS orders (
    id                  BIGSERIAL       PRIMARY KEY,
    user_id             BIGINT          NOT NULL,
    trip_id             BIGINT,
    order_type          VARCHAR(20)     NOT NULL
                            CHECK (order_type IN ('CLOTHING_RENT','CLOTHING_BUY','GUIDE_BOOKING')),
    status              VARCHAR(30)     NOT NULL DEFAULT 'PENDING'
                            CHECK (status IN (
                                'PENDING','CONFIRMED','PROCESSING','SHIPPED',
                                'DELIVERED','CANCELLED','RETURNED',
                                'REFUND_INITIATED','REFUNDED'
                            )),
    total_amount        DECIMAL(10,2)   NOT NULL,
    merchant_id         BIGINT          REFERENCES merchant(id),
    guide_id            BIGINT,
    delivery_address_id BIGINT,
    payment_id          BIGINT,
    rental_start_date   TIMESTAMP,
    rental_end_date     TIMESTAMP,
    notes               TEXT,
    created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_order_rental_dates CHECK (
        order_type <> 'CLOTHING_RENT'
        OR (rental_start_date IS NOT NULL AND rental_end_date IS NOT NULL AND rental_start_date < rental_end_date)
    ),
    CONSTRAINT chk_guide_booking CHECK (
        order_type <> 'GUIDE_BOOKING' OR guide_id IS NOT NULL
    )
);

CREATE INDEX idx_orders_user_id     ON orders(user_id);
CREATE INDEX idx_orders_trip_id     ON orders(trip_id);
CREATE INDEX idx_orders_status      ON orders(status);
CREATE INDEX idx_orders_merchant_id ON orders(merchant_id);
CREATE INDEX idx_orders_type        ON orders(order_type);
