package com.roamster.photo.service;

import com.roamster.photo.dto.PhotoDTOs.RecommendationRequest;
import com.roamster.photo.model.PhotoRecommendationDetail;
import org.springframework.stereotype.Component;

/**
 * Rule-based photo/pose recommendation engine.
 * Determines best time, angle, pose and lighting based on trip context.
 */
@Component
public class PhotoRuleEngine {

    public PhotoRecommendationDetail derivePhotoDetail(RecommendationRequest req, Long recommendationId) {
        PhotoRecommendationDetail.PhotoRecommendationDetailBuilder builder =
                PhotoRecommendationDetail.builder()
                        .recommendationId(recommendationId)
                        .tripId(req.getTripId());

        applyTimeAndLightingRules(builder, req);
        applyCameraRules(builder, req);
        applyPoseRules(builder, req);
        applyLocationRules(builder, req);
        applyOutfitCompatibility(builder, req);

        return builder.build();
    }

    private void applyTimeAndLightingRules(PhotoRecommendationDetail.PhotoRecommendationDetailBuilder b,
                                           RecommendationRequest req) {
        String time = req.getTimeOfDay() != null ? req.getTimeOfDay().toUpperCase() : "MORNING";

        switch (time) {
            case "MORNING" -> {
                b.bestTimeOfDay("MORNING");
                b.lightingCondition("NATURAL");
            }
            case "EVENING" -> {
                b.bestTimeOfDay("GOLDEN_HOUR");
                b.lightingCondition("GOLDEN_HOUR");
            }
            case "NIGHT" -> {
                b.bestTimeOfDay("NIGHT");
                b.lightingCondition("ARTIFICIAL");
                b.lensSuggestion("WIDE");
            }
            default -> {
                b.bestTimeOfDay("MORNING");
                b.lightingCondition("NATURAL");
            }
        }

        if (b.build().getLensSuggestion() == null) {
            b.lensSuggestion("NORMAL");
        }
    }

    private void applyCameraRules(PhotoRecommendationDetail.PhotoRecommendationDetailBuilder b,
                                  RecommendationRequest req) {
        String style = req.getTravelStyle() != null ? req.getTravelStyle().toUpperCase() : "RELAXED";
        boolean socialMedia = Boolean.TRUE.equals(req.getSocialMediaGoal());

        if (socialMedia || "AESTHETIC".equals(style)) {
            b.cameraAngle("LOW");
            b.cameraOrientation("PORTRAIT");
            b.lensSuggestion("WIDE");
        } else if ("ADVENTURE".equals(style)) {
            b.cameraAngle("WIDE");
            b.cameraOrientation("LANDSCAPE");
            b.lensSuggestion("WIDE");
        } else {
            b.cameraAngle("EYE_LEVEL");
            b.cameraOrientation("PORTRAIT");
        }
    }

    private void applyPoseRules(PhotoRecommendationDetail.PhotoRecommendationDetailBuilder b,
                                RecommendationRequest req) {
        String travelType = req.getTravelType() != null ? req.getTravelType().toUpperCase() : "SOLO";
        boolean socialMedia = Boolean.TRUE.equals(req.getSocialMediaGoal());
        String activity = req.getActivityIntensity() != null ? req.getActivityIntensity().toUpperCase() : "MEDIUM";

        if ("HIGH".equals(activity)) {
            b.suggestedPose("Action pose — mid-jump or mid-stride captures energy");
            b.bodyMovementLevel("HIGH");
            b.requiresSpace(true);
        } else if ("COUPLE".equals(travelType)) {
            b.suggestedPose("Candid walk-together or forehead-touch pose; avoid stiff poses");
            b.bodyMovementLevel("LOW");
            b.requiresSpace(false);
        } else if (socialMedia) {
            b.suggestedPose("Facing away from camera looking at the scenery; or candid laughing pose");
            b.bodyMovementLevel("LOW");
            b.requiresSpace(false);
        } else {
            b.suggestedPose("Relaxed standing pose, slight turn to the side, eye-level camera");
            b.bodyMovementLevel("LOW");
            b.requiresSpace(false);
        }
    }

    private void applyLocationRules(PhotoRecommendationDetail.PhotoRecommendationDetailBuilder b,
                                    RecommendationRequest req) {
        String locType = req.getLocationType() != null ? req.getLocationType().toUpperCase() : "OUTDOOR";
        b.locationType(locType);

        if ("ROOFTOP".equals(locType)) {
            b.cameraAngle("LOW");
            b.lensSuggestion("WIDE");
        } else if ("WATERFRONT".equals(locType)) {
            b.lightingCondition("GOLDEN_HOUR");
            b.bestTimeOfDay("EVENING");
        }
    }

    private void applyOutfitCompatibility(PhotoRecommendationDetail.PhotoRecommendationDetailBuilder b,
                                          RecommendationRequest req) {
        if (req.getColorCombination() != null) {
            b.outfitCompatibility("Outfit colors (" + req.getColorCombination() + ") complement natural backgrounds well.");
        } else {
            b.outfitCompatibility("Wear solid, vibrant colors for best contrast against backgrounds.");
        }
    }
}
