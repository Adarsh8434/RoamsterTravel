package com.roamster.clothing.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Clothing-specific detail linked to a base Recommendation.
 * Mirrors 'clothing_recommendation_detail' table.
 */
@Entity
@Table(name = "clothing_recommendation_detail")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClothingRecommendationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id", nullable = false)
    private Recommendation recommendation;

    @Column(name = "clothing_type", length = 50)
    private String clothingType; // CASUAL | FORMAL | TRADITIONAL | SPORTS

    @Column(name = "fabric_type", length = 50)
    private String fabricType; // COTTON | LINEN | WOOL | SYNTHETIC

    @Column(name = "weather_suitability", length = 20)
    private String weatherSuitability; // HOT | COLD | RAINY | ALL

    @Column(name = "comfort_level", length = 20)
    private String comfortLevel; // LOW | MEDIUM | HIGH

    @Column(name = "footwear_suggestion", length = 100)
    private String footwearSuggestion; // SNEAKERS | SANDALS | TREKKING_BOOTS

    @Column(name = "safety_note", columnDefinition = "TEXT")
    private String safetyNote;

    @Column(name = "color_combination", length = 100)
    private String colorCombination;

    @Column(name = "accessories", length = 200)
    private String accessories;

    @Column(name = "image_reference_url", length = 500)
    private String imageReferenceUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
