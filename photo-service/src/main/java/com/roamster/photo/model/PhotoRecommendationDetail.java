package com.roamster.photo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Mirrors 'photo_recommendation_detail' table from DB schema.
 */
@Entity
@Table(name = "photo_recommendation_detail")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PhotoRecommendationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recommendation_id", nullable = false)
    private Long recommendationId;

    @Column(name = "trip_id", nullable = false)
    private Long tripId;

    @Column(name = "best_time_of_day", length = 20)
    private String bestTimeOfDay;       // MORNING | AFTERNOON | EVENING | NIGHT | GOLDEN_HOUR

    @Column(name = "camera_angle", length = 30)
    private String cameraAngle;         // LOW | HIGH | EYE_LEVEL | WIDE

    @Column(name = "camera_orientation", length = 20)
    private String cameraOrientation;   // PORTRAIT | LANDSCAPE

    @Column(name = "suggested_pose", columnDefinition = "TEXT")
    private String suggestedPose;       // e.g. "Facing away from camera"

    @Column(name = "lighting_condition", length = 50)
    private String lightingCondition;   // NATURAL | GOLDEN_HOUR | ARTIFICIAL

    @Column(name = "lens_suggestion", length = 50)
    private String lensSuggestion;      // WIDE | NORMAL | TELEPHOTO

    @Column(name = "location_type", length = 50)
    private String locationType;        // INDOOR | OUTDOOR | ROOFTOP | WATERFRONT

    @Column(name = "body_movement_level", length = 20)
    private String bodyMovementLevel;   // LOW | MEDIUM | HIGH

    @Column(name = "requires_space")
    private Boolean requiresSpace;

    @Column(name = "outfit_compatibility", length = 100)
    private String outfitCompatibility; // e.g. "Works best with vibrant colors"

    @Column(name = "image_reference_url", length = 500)
    private String imageReferenceUrl;   // Example pose image

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
