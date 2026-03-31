package com.roamster.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class EcommerceDTOs {

    // ─── Order ────────────────────────────────────────────────────────────────

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class OrderRequest {
        @NotNull private Long userId;
        @NotNull private Long tripId;
        @NotNull private String orderType;        // CLOTHING_RENT | CLOTHING_BUY | GUIDE_BOOKING
        @NotNull private BigDecimal totalAmount;
        private Long merchantId;
        private Long guideId;
        private Long deliveryAddressId;
        private LocalDateTime rentalStartDate;
        private LocalDateTime rentalEndDate;
        private String notes;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class OrderResponse {
        private Long orderId;
        private String orderType;
        private String status;
        private BigDecimal totalAmount;
        private Long merchantId;
        private Long guideId;
        private Long deliveryAddressId;
        private Long paymentId;
        private LocalDateTime rentalStartDate;
        private LocalDateTime rentalEndDate;
        private LocalDateTime createdAt;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class OrderStatusUpdateRequest {
        @NotNull private String status;
    }

    // ─── Merchant ─────────────────────────────────────────────────────────────

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class MerchantRequest {
        @NotNull private String name;
        @NotNull private String type;
        private String contactNumber;
        private String city;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class MerchantResponse {
        private Long merchantId;
        private String name;
        private String type;
        private String contactNumber;
        private String city;
        private Double rating;
        private Boolean isActive;
    }

    // ─── Inventory summary (future expansion) ────────────────────────────────

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class MerchantInventorySummary {
        private Long merchantId;
        private String merchantName;
        private List<String> availableCategories;
        private Integer totalItems;
    }
}
