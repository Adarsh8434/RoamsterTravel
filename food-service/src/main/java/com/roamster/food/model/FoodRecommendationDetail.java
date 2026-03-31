package com.roamster.food.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Mirrors 'food_recommendation_detail' table.
 * Linked One-to-One from a base Recommendation (category = FOOD).
 */
@Entity
@Table(name = "food_recommendation_detail")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FoodRecommendationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recommendation_id", nullable = false)
    private Long recommendationId;

    @Column(name = "food_type", length = 20)
    private String foodType;        // VEG | NON_VEG | VEGAN | JAIN

    @Column(name = "cuisine_type", length = 50)
    private String cuisineType;     // LOCAL | CONTINENTAL | STREET | TRADITIONAL

    @Column(name = "spice_level", length = 20)
    private String spiceLevel;      // LOW | MEDIUM | HIGH

    @Column(name = "suitable_for", length = 50)
    private String suitableFor;     // SOLO | FAMILY | KIDS | ELDERLY | ALL

    @Column(name = "best_time", length = 20)
    private String bestTime;        // MORNING | AFTERNOON | EVENING | NIGHT

    @Column(name = "hygiene_level", length = 20)
    private String hygieneLevel;    // LOW | MEDIUM | HIGH

    @Column(name = "allergy_warning", columnDefinition = "TEXT")
    private String allergyWarning;

    @Column(name = "dish_names", length = 500)
    private String dishNames;       // Comma-separated suggested dish names

    @Column(name = "image_reference_url", length = 500)
    private String imageReferenceUrl;

    @Column(name = "trip_id", nullable = false)
    private Long tripId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
