package com.roamster.clothing.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a clothing rent/buy order placed by a user.
 * ORDER_TYPE: RENT | BUY
 * STATUS: PENDING | CONFIRMED | SHIPPED | DELIVERED | CANCELLED | RETURNED
 */
@Entity
@Table(name = "clothing_order")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClothingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "trip_id", nullable = false)
    private Long tripId;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    @Column(name = "recommendation_id")
    private Long recommendationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false, length = 10)
    private OrderType orderType; // RENT | BUY

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "delivery_address_id")
    private Long deliveryAddressId;

    @Column(name = "payment_id")
    private Long paymentId;

    // For rental orders: rental period
    @Column(name = "rental_start_date")
    private LocalDateTime rentalStartDate;

    @Column(name = "rental_end_date")
    private LocalDateTime rentalEndDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum OrderType {
        RENT, BUY
    }

    public enum OrderStatus {
        PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED, RETURNED
    }
}
