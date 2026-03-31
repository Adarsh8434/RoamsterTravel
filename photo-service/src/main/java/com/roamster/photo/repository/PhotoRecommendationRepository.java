package com.roamster.photo.repository;

import com.roamster.photo.model.PhotoRecommendationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRecommendationRepository extends JpaRepository<PhotoRecommendationDetail, Long> {

    List<PhotoRecommendationDetail> findByTripId(Long tripId);

    List<PhotoRecommendationDetail> findByTripIdAndBestTimeOfDay(Long tripId, String bestTimeOfDay);

    List<PhotoRecommendationDetail> findByTripIdAndLocationType(Long tripId, String locationType);
}
