package com.roamster.clothing.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Stores base recommendation record.
 * Mirrors the 'recommendations' table in DB schema.
 */
@Entity
@Table(name = "recommendations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", length = 50, nullable = false)
    private String category; // CLOTHING

    @Column(name = "target_entity_type", length = 50)
    private String targetEntityType; // MERCHANT | GUIDE | SYSTEM

    @Column(name = "target_entity_id")
    private Long targetEntityId;

    @Column(name = "confidence_score", precision = 5, scale = 2)
    private BigDecimal confidenceScore;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "is_safe")
    private Boolean isSafe;

    @Column(name = "generation_source", length = 20)
    private String generationSource; // ML | RULE | MANUAL

    @Column(name = "trip_id", nullable = false)
    private Long tripId;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // One-to-One relationship to clothing detail
    @OneToOne(mappedBy = "recommendation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ClothingRecommendationDetail clothingDetail;
}
