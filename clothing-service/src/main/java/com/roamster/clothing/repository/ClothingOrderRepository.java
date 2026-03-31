package com.roamster.clothing.repository;

import com.roamster.clothing.model.ClothingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClothingOrderRepository extends JpaRepository<ClothingOrder, Long> {

    List<ClothingOrder> findByUserId(Long userId);

    List<ClothingOrder> findByTripId(Long tripId);

    List<ClothingOrder> findByUserIdAndOrderType(Long userId, ClothingOrder.OrderType orderType);

    List<ClothingOrder> findByMerchantIdAndStatus(Long merchantId, ClothingOrder.OrderStatus status);
}
