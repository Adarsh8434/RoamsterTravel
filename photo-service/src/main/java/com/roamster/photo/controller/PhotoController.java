package com.roamster.photo.controller;

import com.roamster.photo.dto.PhotoDTOs.*;
import com.roamster.photo.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/photo")
@RequiredArgsConstructor
@Tag(name = "Photo / Best Shot Service", description = "Photo pose, angle, and timing recommendations")
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/recommendations")
    @Operation(summary = "Generate a best-shot recommendation for a trip")
    public ResponseEntity<RecommendationResponse> generateRecommendation(
            @Valid @RequestBody RecommendationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(photoService.generateRecommendation(request));
    }

    @GetMapping("/recommendations/trip/{tripId}")
    @Operation(summary = "Get all photo recommendations for a trip")
    public ResponseEntity<List<RecommendationResponse>> getForTrip(@PathVariable Long tripId) {
        return ResponseEntity.ok(photoService.getRecommendationsForTrip(tripId));
    }

    @GetMapping("/recommendations/trip/{tripId}/time")
    @Operation(summary = "Get photo recommendations filtered by time of day")
    public ResponseEntity<List<RecommendationResponse>> getByTimeOfDay(
            @PathVariable Long tripId,
            @RequestParam String timeOfDay) {
        return ResponseEntity.ok(photoService.getByTimeOfDay(tripId, timeOfDay));
    }
}
