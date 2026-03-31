package com.roamster.photo.service;

import com.roamster.photo.dto.PhotoDTOs.*;
import com.roamster.photo.messaging.PhotoEventPublisher;
import com.roamster.photo.model.PhotoRecommendationDetail;
import com.roamster.photo.repository.PhotoRecommendationRepository;
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
public class PhotoService {

    private final PhotoRecommendationRepository photoRepository;
    private final PhotoEventPublisher eventPublisher;
    private final PhotoRuleEngine ruleEngine;

    @Transactional
    public RecommendationResponse generateRecommendation(RecommendationRequest request) {
        log.info("Generating photo recommendation for tripId={}", request.getTripId());

        Long recommendationId = System.currentTimeMillis(); // stub — link to recommendations table via FK
        PhotoRecommendationDetail detail = ruleEngine.derivePhotoDetail(request, recommendationId);
        detail = photoRepository.save(detail);

        eventPublisher.publishRecommendationGenerated(detail.getId(), request.getTripId());

        return mapToResponse(detail, new BigDecimal("0.88"),
                buildReason(request), LocalDateTime.now().plusDays(1));
    }

    public List<RecommendationResponse> getRecommendationsForTrip(Long tripId) {
        return photoRepository.findByTripId(tripId)
                .stream()
                .map(d -> mapToResponse(d, new BigDecimal("0.88"), null, null))
                .collect(Collectors.toList());
    }

    public List<RecommendationResponse> getByTimeOfDay(Long tripId, String timeOfDay) {
        return photoRepository.findByTripIdAndBestTimeOfDay(tripId, timeOfDay.toUpperCase())
                .stream()
                .map(d -> mapToResponse(d, new BigDecimal("0.88"), null, null))
                .collect(Collectors.toList());
    }

    private String buildReason(RecommendationRequest req) {
        return String.format("Optimised for %s location, %s lighting at %s time, %s travel style.",
                req.getLocationType(), req.getWeather(), req.getTimeOfDay(), req.getTravelStyle());
    }

    private RecommendationResponse mapToResponse(PhotoRecommendationDetail d,
                                                  BigDecimal score, String reason, LocalDateTime validUntil) {
        return RecommendationResponse.builder()
                .recommendationId(d.getId())
                .tripId(d.getTripId())
                .bestTimeOfDay(d.getBestTimeOfDay())
                .cameraAngle(d.getCameraAngle())
                .cameraOrientation(d.getCameraOrientation())
                .suggestedPose(d.getSuggestedPose())
                .lightingCondition(d.getLightingCondition())
                .lensSuggestion(d.getLensSuggestion())
                .locationType(d.getLocationType())
                .bodyMovementLevel(d.getBodyMovementLevel())
                .requiresSpace(d.getRequiresSpace())
                .outfitCompatibility(d.getOutfitCompatibility())
                .imageReferenceUrl(d.getImageReferenceUrl())
                .confidenceScore(score)
                .reason(reason)
                .validUntil(validUntil)
                .build();
    }
}
