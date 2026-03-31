package com.roamster.clothing.controller;

import com.roamster.clothing.dto.ClothingDTOs.*;
import com.roamster.clothing.service.ClothingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clothing")
@RequiredArgsConstructor
@Tag(name = "Clothing Service", description = "Clothing recommendations and rent/buy orders")
public class ClothingController {

    private final ClothingService clothingService;

    // ─── Recommendations ─────────────────────────────────────────────────────

    @PostMapping("/recommendations")
    @Operation(summary = "Generate a clothing recommendation for a trip")
    public ResponseEntity<RecommendationResponse> generateRecommendation(
            @Valid @RequestBody RecommendationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clothingService.generateRecommendation(request));
    }

    @GetMapping("/recommendations/trip/{tripId}")
    @Operation(summary = "Get all clothing recommendations for a trip")
    public ResponseEntity<List<RecommendationResponse>> getRecommendationsForTrip(
            @Parameter(description = "Trip ID") @PathVariable Long tripId) {
        return ResponseEntity.ok(clothingService.getRecommendationsForTrip(tripId));
    }

    // ─── Orders (Rent / Buy) ──────────────────────────────────────────────────

    @PostMapping("/orders")
    @Operation(summary = "Place a clothing rent or buy order")
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clothingService.placeOrder(request));
    }

    @PatchMapping("/orders/{orderId}/status")
    @Operation(summary = "Update clothing order status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        return ResponseEntity.ok(clothingService.updateOrderStatus(orderId, status));
    }

    @GetMapping("/orders/user/{userId}")
    @Operation(summary = "Get all clothing orders for a user")
    public ResponseEntity<List<OrderResponse>> getUserOrders(
            @PathVariable Long userId) {
        return ResponseEntity.ok(clothingService.getUserOrders(userId));
    }
}
