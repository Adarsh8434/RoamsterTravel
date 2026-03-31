package com.roamster.food.repository;

import com.roamster.food.model.FoodRecommendationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRecommendationRepository extends JpaRepository<FoodRecommendationDetail, Long> {

    List<FoodRecommendationDetail> findByTripId(Long tripId);

    @Query("SELECT f FROM FoodRecommendationDetail f WHERE f.tripId = :tripId AND f.bestTime = :timeOfDay")
    List<FoodRecommendationDetail> findByTripIdAndBestTime(Long tripId, String timeOfDay);

    List<FoodRecommendationDetail> findByTripIdAndFoodType(Long tripId, String foodType);
}
