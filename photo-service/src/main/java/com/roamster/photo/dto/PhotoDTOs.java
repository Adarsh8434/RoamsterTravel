package com.roamster.photo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PhotoDTOs {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RecommendationRequest {
        @NotNull private Long tripId;
        @NotNull private Long userId;

        // Context
        private String destination;
        private String locationType;        // INDOOR | OUTDOOR | ROOFTOP | WATERFRONT
        private String timeOfDay;           // MORNING | AFTERNOON | EVENING | NIGHT
        private String season;
        private String weather;

        // User intent
        private String travelStyle;         // RELAXED | ADVENTURE | AESTHETIC
        private Boolean socialMediaGoal;    // true = optimise for reels/posts
        private String activityIntensity;   // LOW | MEDIUM | HIGH

        // Travel group
        private String travelType;          // SOLO | COUPLE | FAMILY
        private Boolean hasKids;
        private Boolean hasElderly;

        // Clothing context (cross-service hint)
        private String clothingType;        // forwarded from clothing recommendation
        private String colorCombination;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RecommendationResponse {
        private Long recommendationId;
        private Long tripId;
        private String bestTimeOfDay;
        private String cameraAngle;
        private String cameraOrientation;
        private String suggestedPose;
        private String lightingCondition;
        private String lensSuggestion;
        private String locationType;
        private String bodyMovementLevel;
        private Boolean requiresSpace;
        private String outfitCompatibility;
        private String imageReferenceUrl;
        private BigDecimal confidenceScore;
        private String reason;
        private LocalDateTime validUntil;
    }
}
