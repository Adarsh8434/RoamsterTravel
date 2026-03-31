package com.roamster.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Central Order table — covers clothing rent/buy and guide bookings.
 * Mirrors the 'order' table in schema.
 */
@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "trip_id")
    private Long tripId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false, length = 20)
    private OrderType orderType; // CLOTHING_RENT | CLOTHING_BUY | GUIDE_BOOKING

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "merchant_id")
    private Long merchantId;  // nullable for guide orders

    @Column(name = "guide_id")
    private Long guideId;     // nullable for merchant orders

    @Column(name = "delivery_address_id")
    private Long deliveryAddressId; // nullable for guide bookings

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "rental_start_date")
    private LocalDateTime rentalStartDate;

    @Column(name = "rental_end_date")
    private LocalDateTime rentalEndDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum OrderType {
        CLOTHING_RENT, CLOTHING_BUY, GUIDE_BOOKING
    }

    public enum OrderStatus {
        PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED,
        CANCELLED, RETURNED, REFUND_INITIATED, REFUNDED
    }
}
