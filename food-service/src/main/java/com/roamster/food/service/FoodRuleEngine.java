package com.roamster.food.service;

import com.roamster.food.dto.FoodDTOs.RecommendationRequest;
import com.roamster.food.model.FoodRecommendationDetail;
import org.springframework.stereotype.Component;

/**
 * Rule-based food recommendation engine.
 * Derives food suggestions from trip/user context parameters.
 */
@Component
public class FoodRuleEngine {

    public FoodRecommendationDetail deriveFoodDetail(RecommendationRequest req, Long recommendationId) {
        FoodRecommendationDetail.FoodRecommendationDetailBuilder builder =
                FoodRecommendationDetail.builder()
                        .recommendationId(recommendationId)
                        .tripId(req.getTripId());

        applyDietaryRules(builder, req);
        applyGroupRules(builder, req);
        applyTimeRules(builder, req);
        applyAllergyRules(builder, req);

        return builder.build();
    }

    private void applyDietaryRules(FoodRecommendationDetail.FoodRecommendationDetailBuilder b,
                                   RecommendationRequest req) {
        String diet = req.getDietaryPreference() != null ? req.getDietaryPreference().toUpperCase() : "VEG";

        b.foodType(diet);
        b.spiceLevel(req.getSpicePreference() != null ? req.getSpicePreference().toUpperCase() : "MEDIUM");

        switch (diet) {
            case "VEGAN" -> {
                b.cuisineType("LOCAL");
                b.dishNames("Fresh salad bowls, Fruit platters, Stir-fried vegetables");
                b.hygieneLevel("HIGH");
            }
            case "JAIN" -> {
                b.cuisineType("TRADITIONAL");
                b.dishNames("Jain thali, Farsan, Rotla");
                b.hygieneLevel("HIGH");
            }
            case "NON_VEG" -> {
                b.cuisineType("LOCAL");
                b.dishNames("Local non-veg thali, Grilled meats, Regional seafood");
                b.hygieneLevel("MEDIUM");
            }
            default -> {
                b.cuisineType("LOCAL");
                b.dishNames("Local veg thali, Dal baati, Street chaat");
                b.hygieneLevel("MEDIUM");
            }
        }
    }

    private void applyGroupRules(FoodRecommendationDetail.FoodRecommendationDetailBuilder b,
                                 RecommendationRequest req) {
        if (Boolean.TRUE.equals(req.getHasKids())) {
            b.suitableFor("KIDS");
            b.spiceLevel("LOW");
            b.dishNames("Mild khichdi, Plain rice dishes, Soft rotis, Fruit platters");
        } else if (Boolean.TRUE.equals(req.getHasElderly())) {
            b.suitableFor("ELDERLY");
            b.spiceLevel("LOW");
            b.dishNames("Soft cooked meals, Soups, Light dals");
        } else {
            String travelType = req.getTravelType() != null ? req.getTravelType().toUpperCase() : "SOLO";
            b.suitableFor(switch (travelType) {
                case "COUPLE" -> "COUPLE";
                case "FAMILY" -> "FAMILY";
                default -> "SOLO";
            });
        }
    }

    private void applyTimeRules(FoodRecommendationDetail.FoodRecommendationDetailBuilder b,
                                RecommendationRequest req) {
        String timeOfDay = req.getTimeOfDay() != null ? req.getTimeOfDay().toUpperCase() : "AFTERNOON";

        b.bestTime(timeOfDay);

        switch (timeOfDay) {
            case "MORNING" -> b.dishNames("Poha, Upma, Idli-Sambar, Fresh juice");
            case "AFTERNOON" -> b.dishNames("Full thali, Biryani, Regional lunch special");
            case "EVENING" -> b.dishNames("Street snacks, Chai, Samosa, Chaat");
            case "NIGHT" -> b.dishNames("Dinner thali, Roti-sabzi, Light soups");
        }
    }

    private void applyAllergyRules(FoodRecommendationDetail.FoodRecommendationDetailBuilder b,
                                   RecommendationRequest req) {
        if (req.getAllergyInfo() != null && !req.getAllergyInfo().isBlank()) {
            b.allergyWarning("User has reported allergies: " + req.getAllergyInfo() +
                    ". Please verify ingredients before ordering.");
        } else {
            b.allergyWarning("No specific allergies reported. Always ask staff about common allergens.");
        }
    }
}
