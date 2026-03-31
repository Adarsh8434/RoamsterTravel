package com.roamster.clothing.service;

import com.roamster.clothing.dto.ClothingDTOs.RecommendationRequest;
import com.roamster.clothing.model.ClothingRecommendationDetail;
import org.springframework.stereotype.Component;

/**
 * Rule-based clothing recommendation logic.
 * Pluggable: ML engine can replace or override these rules in future.
 */
@Component
public class ClothingRuleEngine {

    public ClothingRecommendationDetail deriveClothingDetail(RecommendationRequest req) {
        ClothingRecommendationDetail.ClothingRecommendationDetailBuilder builder =
                ClothingRecommendationDetail.builder();

        applyWeatherRules(builder, req);
        applyActivityRules(builder, req);
        applyStyleRules(builder, req);
        applySafetyRules(builder, req);

        return builder.build();
    }

    private void applyWeatherRules(ClothingRecommendationDetail.ClothingRecommendationDetailBuilder b,
                                   RecommendationRequest req) {
        String weather = req.getWeather() != null ? req.getWeather().toUpperCase() : "ALL";

        switch (weather) {
            case "HOT" -> {
                b.fabricType("COTTON");
                b.weatherSuitability("HOT");
                b.clothingType("CASUAL");
                b.footwearSuggestion("SANDALS");
                b.colorCombination("Light pastels, whites, beige");
            }
            case "COLD" -> {
                b.fabricType("WOOL");
                b.weatherSuitability("COLD");
                b.clothingType("CASUAL");
                b.footwearSuggestion("BOOTS");
                b.colorCombination("Earthy tones, dark blues, greys");
            }
            case "RAINY" -> {
                b.fabricType("SYNTHETIC");
                b.weatherSuitability("RAINY");
                b.clothingType("SPORTS");
                b.footwearSuggestion("WATERPROOF_SHOES");
                b.colorCombination("Dark colors to hide rain spots");
            }
            default -> {
                b.fabricType("COTTON");
                b.weatherSuitability("ALL");
                b.clothingType("CASUAL");
                b.footwearSuggestion("SNEAKERS");
                b.colorCombination("Neutral tones");
            }
        }
    }

    private void applyActivityRules(ClothingRecommendationDetail.ClothingRecommendationDetailBuilder b,
                                    RecommendationRequest req) {
        String intensity = req.getActivityIntensity() != null ? req.getActivityIntensity().toUpperCase() : "MEDIUM";

        switch (intensity) {
            case "HIGH" -> {
                b.clothingType("SPORTS");
                b.comfortLevel("HIGH");
                b.accessories("Sports watch, compression socks");
            }
            case "LOW" -> {
                b.comfortLevel("HIGH");
                b.accessories("Sunglasses, light scarf");
            }
            default -> {
                b.comfortLevel("MEDIUM");
                b.accessories("Sunglasses, casual backpack");
            }
        }
    }

    private void applyStyleRules(ClothingRecommendationDetail.ClothingRecommendationDetailBuilder b,
                                 RecommendationRequest req) {
        String style = req.getTravelStyle() != null ? req.getTravelStyle().toUpperCase() : "RELAXED";

        if ("AESTHETIC".equals(style) && Boolean.TRUE.equals(req.getSocialMediaGoal())) {
            b.colorCombination("Vibrant coordinates, solid contrast colors");
            b.accessories("Statement accessories, photo-ready outfit");
        }

        if ("ADVENTURE".equals(style)) {
            b.clothingType("SPORTS");
            b.footwearSuggestion("TREKKING_BOOTS");
        }
    }

    private void applySafetyRules(ClothingRecommendationDetail.ClothingRecommendationDetailBuilder b,
                                  RecommendationRequest req) {
        StringBuilder safetyNote = new StringBuilder();

        if (Boolean.TRUE.equals(req.getHasKids())) {
            safetyNote.append("Avoid loose clothing and open-toe shoes for kids. ");
        }
        if (Boolean.TRUE.equals(req.getHasElderly())) {
            safetyNote.append("Ensure non-slip footwear for elderly members. ");
        }
        if ("HIGH".equalsIgnoreCase(req.getActivityIntensity())) {
            safetyNote.append("Avoid loose clothing near machinery or adventure equipment. ");
        }

        b.safetyNote(safetyNote.toString().trim());
    }
}
