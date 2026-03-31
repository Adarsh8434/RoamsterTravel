package com.roamster.food.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FoodDTOs {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RecommendationRequest {
        @NotNull private Long tripId;
        @NotNull private Long userId;

        // Trip context
        private String destination;
        private String season;
        private String weather;
        private String timeOfDay;       // MORNING | AFTERNOON | EVENING | NIGHT

        // User profile
        private String dietaryPreference; // VEG | NON_VEG | VEGAN | JAIN
        private String ageGroup;

        // Travel group
        private String travelType;       // SOLO | COUPLE | FAMILY | KIDS | ELDERLY
        private Boolean hasKids;
        private Boolean hasElderly;

        // Preferences
        private String spicePreference;  // LOW | MEDIUM | HIGH
        private String comfortLevel;     // BASIC | MODERATE | PREMIUM
        private String allergyInfo;      // Free text, e.g. "Peanuts, Gluten"
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RecommendationResponse {
        private Long recommendationId;
        private Long tripId;
        private String foodType;
        private String cuisineType;
        private String spiceLevel;
        private String suitableFor;
        private String bestTime;
        private String hygieneLevel;
        private String allergyWarning;
        private String dishNames;
        private String imageReferenceUrl;
        private BigDecimal confidenceScore;
        private String reason;
        private LocalDateTime validUntil;
    }
}
