package com.roamster.food.controller;

import com.roamster.food.dto.FoodDTOs.*;
import com.roamster.food.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/food")
@RequiredArgsConstructor
@Tag(name = "Food Service", description = "Food recommendations based on trip context and preferences")
public class FoodController {

    private final FoodService foodService;

    @PostMapping("/recommendations")
    @Operation(summary = "Generate a food recommendation for a trip")
    public ResponseEntity<RecommendationResponse> generateRecommendation(
            @Valid @RequestBody RecommendationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(foodService.generateRecommendation(request));
    }

    @GetMapping("/recommendations/trip/{tripId}")
    @Operation(summary = "Get all food recommendations for a trip")
    public ResponseEntity<List<RecommendationResponse>> getForTrip(@PathVariable Long tripId) {
        return ResponseEntity.ok(foodService.getRecommendationsForTrip(tripId));
    }

    @GetMapping("/recommendations/trip/{tripId}/time")
    @Operation(summary = "Get food recommendations filtered by time of day")
    public ResponseEntity<List<RecommendationResponse>> getByTimeOfDay(
            @PathVariable Long tripId,
            @RequestParam String timeOfDay) {
        return ResponseEntity.ok(foodService.getRecommendationsByTimeOfDay(tripId, timeOfDay));
    }
}
