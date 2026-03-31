package com.roamster.clothing.repository;

import com.roamster.clothing.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByTripIdAndCategory(Long tripId, String category);

    @Query("SELECT r FROM Recommendation r WHERE r.tripId = :tripId AND r.category = 'CLOTHING' ORDER BY r.confidenceScore DESC")
    List<Recommendation> findTopClothingRecommendationsByTrip(Long tripId);

    Optional<Recommendation> findTopByTripIdAndCategoryOrderByConfidenceScoreDesc(Long tripId, String category);
}
