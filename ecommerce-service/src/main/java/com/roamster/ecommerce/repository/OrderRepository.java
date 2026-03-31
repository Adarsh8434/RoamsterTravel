package com.roamster.ecommerce.repository;

import com.roamster.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByTripId(Long tripId);
    List<Order> findByMerchantIdAndStatus(Long merchantId, Order.OrderStatus status);
    List<Order> findByUserIdAndOrderType(Long userId, Order.OrderType orderType);
}
