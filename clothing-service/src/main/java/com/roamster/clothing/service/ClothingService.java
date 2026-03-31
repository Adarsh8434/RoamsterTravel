package com.roamster.clothing.service;

import com.roamster.clothing.dto.ClothingDTOs.*;
import com.roamster.clothing.messaging.ClothingEventPublisher;
import com.roamster.clothing.model.ClothingOrder;
import com.roamster.clothing.model.ClothingRecommendationDetail;
import com.roamster.clothing.model.Recommendation;
import com.roamster.clothing.repository.ClothingOrderRepository;
import com.roamster.clothing.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClothingService {

    private final RecommendationRepository recommendationRepository;
    private final ClothingOrderRepository clothingOrderRepository;
    private final ClothingEventPublisher eventPublisher;
    private final ClothingRuleEngine ruleEngine;

    // ─── Recommendation ──────────────────────────────────────────────────────

    @Transactional
    public RecommendationResponse generateRecommendation(RecommendationRequest request) {
        log.info("Generating clothing recommendation for tripId={}, userId={}", request.getTripId(), request.getUserId());

        // Apply rule-based logic to derive clothing suggestion
        ClothingRecommendationDetail detail = ruleEngine.deriveClothingDetail(request);

        // Persist base recommendation
        Recommendation recommendation = Recommendation.builder()
                .category("CLOTHING")
                .targetEntityType("SYSTEM")
                .tripId(request.getTripId())
                .confidenceScore(new BigDecimal("0.85"))
                .reason(buildReason(request))
                .isSafe(true)
                .generationSource("RULE")
                .validUntil(LocalDateTime.now().plusDays(3))
                .build();

        recommendation = recommendationRepository.save(recommendation);
        detail.setRecommendation(recommendation);
        recommendation.setClothingDetail(detail);
        recommendationRepository.save(recommendation);

        // Publish event to ML service for model improvement
        eventPublisher.publishRecommendationGenerated(recommendation.getId(), request.getTripId());

        return mapToResponse(recommendation, detail);
    }

    public List<RecommendationResponse> getRecommendationsForTrip(Long tripId) {
        return recommendationRepository.findTopClothingRecommendationsByTrip(tripId)
                .stream()
                .map(r -> mapToResponse(r, r.getClothingDetail()))
                .collect(Collectors.toList());
    }

    // ─── Order (Rent / Buy) ───────────────────────────────────────────────────

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        log.info("Placing {} order for userId={}, tripId={}", request.getOrderType(), request.getUserId(), request.getTripId());

        validateOrderRequest(request);

        ClothingOrder order = ClothingOrder.builder()
                .userId(request.getUserId())
                .tripId(request.getTripId())
                .merchantId(request.getMerchantId())
                .recommendationId(request.getRecommendationId())
                .orderType(ClothingOrder.OrderType.valueOf(request.getOrderType()))
                .status(ClothingOrder.OrderStatus.PENDING)
                .totalAmount(request.getTotalAmount())
                .deliveryAddressId(request.getDeliveryAddressId())
                .rentalStartDate(request.getRentalStartDate())
                .rentalEndDate(request.getRentalEndDate())
                .build();

        order = clothingOrderRepository.save(order);

        // Publish order event → ecommerce-service and notification-service
        eventPublisher.publishOrderPlaced(order.getId(), order.getUserId(), order.getOrderType().name());

        return mapOrderToResponse(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, String newStatus) {
        ClothingOrder order = clothingOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        order.setStatus(ClothingOrder.OrderStatus.valueOf(newStatus));
        order = clothingOrderRepository.save(order);

        eventPublisher.publishOrderStatusUpdated(orderId, newStatus);
        return mapOrderToResponse(order);
    }

    public List<OrderResponse> getUserOrders(Long userId) {
        return clothingOrderRepository.findByUserId(userId)
                .stream().map(this::mapOrderToResponse).collect(Collectors.toList());
    }

    // ─── Private helpers ─────────────────────────────────────────────────────

    private void validateOrderRequest(OrderRequest request) {
        if ("RENT".equals(request.getOrderType())) {
            if (request.getRentalStartDate() == null || request.getRentalEndDate() == null) {
                throw new IllegalArgumentException("Rental orders require start and end dates.");
            }
            if (request.getRentalStartDate().isAfter(request.getRentalEndDate())) {
                throw new IllegalArgumentException("Rental start date must be before end date.");
            }
        }
    }

    private String buildReason(RecommendationRequest req) {
        return String.format("Based on %s weather in %s, %s travel style and %s activity level.",
                req.getWeather(), req.getDestination(), req.getTravelStyle(), req.getActivityIntensity());
    }

    private RecommendationResponse mapToResponse(Recommendation rec, ClothingRecommendationDetail detail) {
        RecommendationResponse.RecommendationResponseBuilder builder = RecommendationResponse.builder()
                .recommendationId(rec.getId())
                .tripId(rec.getTripId())
                .confidenceScore(rec.getConfidenceScore())
                .reason(rec.getReason())
                .generationSource(rec.getGenerationSource())
                .validUntil(rec.getValidUntil());

        if (detail != null) {
            builder.clothingType(detail.getClothingType())
                    .fabricType(detail.getFabricType())
                    .weatherSuitability(detail.getWeatherSuitability())
                    .comfortLevel(detail.getComfortLevel())
                    .footwearSuggestion(detail.getFootwearSuggestion())
                    .colorCombination(detail.getColorCombination())
                    .accessories(detail.getAccessories())
                    .safetyNote(detail.getSafetyNote())
                    .imageReferenceUrl(detail.getImageReferenceUrl());
        }
        return builder.build();
    }

    private OrderResponse mapOrderToResponse(ClothingOrder order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .orderType(order.getOrderType().name())
                .totalAmount(order.getTotalAmount())
                .rentalStartDate(order.getRentalStartDate())
                .rentalEndDate(order.getRentalEndDate())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
