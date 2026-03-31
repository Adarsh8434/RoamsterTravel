-- ============================================================
-- Roamster :: Photo / Best Shot Service
-- Migration: V1__init_photo_schema.sql
-- ============================================================

CREATE TABLE IF NOT EXISTS photo_recommendation_detail (
    id                    BIGSERIAL     PRIMARY KEY,
    recommendation_id     BIGINT        NOT NULL,
    trip_id               BIGINT        NOT NULL,
    best_time_of_day      VARCHAR(20)   CHECK (best_time_of_day IN ('MORNING','AFTERNOON','EVENING','NIGHT','GOLDEN_HOUR')),
    camera_angle          VARCHAR(30)   CHECK (camera_angle IN ('LOW','HIGH','EYE_LEVEL','WIDE')),
    camera_orientation    VARCHAR(20)   CHECK (camera_orientation IN ('PORTRAIT','LANDSCAPE')),
    suggested_pose        TEXT,
    lighting_condition    VARCHAR(50)   CHECK (lighting_condition IN ('NATURAL','GOLDEN_HOUR','ARTIFICIAL')),
    lens_suggestion       VARCHAR(50)   CHECK (lens_suggestion IN ('WIDE','NORMAL','TELEPHOTO')),
    location_type         VARCHAR(50)   CHECK (location_type IN ('INDOOR','OUTDOOR','ROOFTOP','WATERFRONT')),
    body_movement_level   VARCHAR(20)   CHECK (body_movement_level IN ('LOW','MEDIUM','HIGH')),
    requires_space        BOOLEAN       DEFAULT FALSE,
    outfit_compatibility  VARCHAR(200),
    image_reference_url   VARCHAR(500),
    created_at            TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMP     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_photo_rec_trip     ON photo_recommendation_detail(trip_id);
CREATE INDEX idx_photo_rec_time     ON photo_recommendation_detail(best_time_of_day);
CREATE INDEX idx_photo_rec_location ON photo_recommendation_detail(location_type);
