package com.roamster.clothing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// ─── Request DTOs ─────────────────────────────────────────────────────────────

public class ClothingDTOs {

    /**
     * Input needed to generate a clothing recommendation.
     */
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RecommendationRequest {
        @NotNull private Long tripId;
        @NotNull private Long userId;

        // Trip context
        private String destination;
        private String season;       // SUMMER | WINTER | MONSOON | AUTUMN | SPRING
        private String weather;      // HOT | COLD | RAINY | ALL
        private String timeOfDay;    // MORNING | AFTERNOON | EVENING | NIGHT

        // User profile
        private String gender;       // MALE | FEMALE | OTHER
        private String ageGroup;     // 18-30 | 30-45 | 45-60 | 60+
        private String clothingSize; // S | M | L | XL | XXL

        // Travel group
        private String travelType;   // SOLO | COUPLE | FAMILY | KIDS | ELDERLY
        private Integer numberOfPeople;
        private Boolean hasKids;
        private Boolean hasElderly;

        // Preferences
        private String travelStyle;       // RELAXED | ADVENTURE | AESTHETIC
        private String comfortLevel;      // BASIC | MODERATE | PREMIUM
        private String activityIntensity; // LOW | MEDIUM | HIGH
        private Boolean socialMediaGoal;
    }

    /**
     * Full clothing recommendation response.
     */
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RecommendationResponse {
        private Long recommendationId;
        private Long tripId;
        private String clothingType;
        private String fabricType;
        private String weatherSuitability;
        private String comfortLevel;
        private String footwearSuggestion;
        private String colorCombination;
        private String accessories;
        private String safetyNote;
        private String imageReferenceUrl;
        private BigDecimal confidenceScore;
        private String reason;
        private String generationSource;
        private LocalDateTime validUntil;
    }

    /**
     * Request to place a rent or buy order for clothing.
     */
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class OrderRequest {
        @NotNull private Long userId;
        @NotNull private Long tripId;
        @NotNull private Long merchantId;
        @NotNull private Long recommendationId;
        @NotNull private String orderType;          // RENT | BUY
        @NotNull private BigDecimal totalAmount;
        private Long deliveryAddressId;
        private LocalDateTime rentalStartDate;      // Required for RENT
        private LocalDateTime rentalEndDate;        // Required for RENT
    }

    /**
     * Order response after creation.
     */
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class OrderResponse {
        private Long orderId;
        private String status;
        private String orderType;
        private BigDecimal totalAmount;
        private LocalDateTime rentalStartDate;
        private LocalDateTime rentalEndDate;
        private LocalDateTime createdAt;
    }
}
