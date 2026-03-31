package com.roamster.food.service;

import com.roamster.food.dto.FoodDTOs.*;
import com.roamster.food.messaging.FoodEventPublisher;
import com.roamster.food.model.FoodRecommendationDetail;
import com.roamster.food.repository.FoodRecommendationRepository;
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
public class FoodService {

    private final FoodRecommendationRepository foodRepository;
    private final FoodEventPublisher eventPublisher;
    private final FoodRuleEngine ruleEngine;

    @Transactional
    public RecommendationResponse generateRecommendation(RecommendationRequest request) {
        log.info("Generating food recommendation for tripId={}", request.getTripId());

        // Use a synthetic recommendation ID stub (real FK to recommendations table)
        Long recommendationId = System.currentTimeMillis(); // Replace with actual recommendation service call

        FoodRecommendationDetail detail = ruleEngine.deriveFoodDetail(request, recommendationId);
        detail = foodRepository.save(detail);

        eventPublisher.publishRecommendationGenerated(detail.getId(), request.getTripId());

        return mapToResponse(detail, new BigDecimal("0.82"),
                buildReason(request), LocalDateTime.now().plusDays(1));
    }

    public List<RecommendationResponse> getRecommendationsForTrip(Long tripId) {
        return foodRepository.findByTripId(tripId)
                .stream()
                .map(d -> mapToResponse(d, new BigDecimal("0.82"), null, null))
                .collect(Collectors.toList());
    }

    public List<RecommendationResponse> getRecommendationsByTimeOfDay(Long tripId, String timeOfDay) {
        return foodRepository.findByTripIdAndBestTime(tripId, timeOfDay.toUpperCase())
                .stream()
                .map(d -> mapToResponse(d, new BigDecimal("0.82"), null, null))
                .collect(Collectors.toList());
    }

    private String buildReason(RecommendationRequest req) {
        return String.format("Based on %s dietary preference, %s travel group, and %s time context.",
                req.getDietaryPreference(), req.getTravelType(), req.getTimeOfDay());
    }

    private RecommendationResponse mapToResponse(FoodRecommendationDetail d,
                                                  BigDecimal score, String reason, LocalDateTime validUntil) {
        return RecommendationResponse.builder()
                .recommendationId(d.getId())
                .tripId(d.getTripId())
                .foodType(d.getFoodType())
                .cuisineType(d.getCuisineType())
                .spiceLevel(d.getSpiceLevel())
                .suitableFor(d.getSuitableFor())
                .bestTime(d.getBestTime())
                .hygieneLevel(d.getHygieneLevel())
                .allergyWarning(d.getAllergyWarning())
                .dishNames(d.getDishNames())
                .imageReferenceUrl(d.getImageReferenceUrl())
                .confidenceScore(score)
                .reason(reason)
                .validUntil(validUntil)
                .build();
    }
}
