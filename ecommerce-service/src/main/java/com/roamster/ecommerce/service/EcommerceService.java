package com.roamster.ecommerce.service;

import com.roamster.ecommerce.dto.EcommerceDTOs.*;
import com.roamster.ecommerce.messaging.EcommerceEventPublisher;
import com.roamster.ecommerce.model.Merchant;
import com.roamster.ecommerce.model.Order;
import com.roamster.ecommerce.repository.MerchantRepository;
import com.roamster.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EcommerceService {

    private final OrderRepository orderRepository;
    private final MerchantRepository merchantRepository;
    private final EcommerceEventPublisher eventPublisher;

    // ─── Orders ──────────────────────────────────────────────────────────────

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        log.info("Creating order type={} for userId={}", request.getOrderType(), request.getUserId());

        validateOrder(request);

        Order order = Order.builder()
                .userId(request.getUserId())
                .tripId(request.getTripId())
                .orderType(Order.OrderType.valueOf(request.getOrderType()))
                .status(Order.OrderStatus.PENDING)
                .totalAmount(request.getTotalAmount())
                .merchantId(request.getMerchantId())
                .guideId(request.getGuideId())
                .deliveryAddressId(request.getDeliveryAddressId())
                .rentalStartDate(request.getRentalStartDate())
                .rentalEndDate(request.getRentalEndDate())
                .notes(request.getNotes())
                .build();

        order = orderRepository.save(order);
        eventPublisher.publishOrderCreated(order.getId(), order.getUserId(), order.getOrderType().name());
        return mapOrderToResponse(order);
    }

    @Transactional
    public OrderResponse updateStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        order.setStatus(Order.OrderStatus.valueOf(newStatus));
        order = orderRepository.save(order);

        eventPublisher.publishOrderStatusChanged(orderId, newStatus, order.getUserId());
        return mapOrderToResponse(order);
    }

    public OrderResponse getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::mapOrderToResponse)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
    }

    public List<OrderResponse> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapOrderToResponse).collect(Collectors.toList());
    }

    public List<OrderResponse> getTripOrders(Long tripId) {
        return orderRepository.findByTripId(tripId).stream()
                .map(this::mapOrderToResponse).collect(Collectors.toList());
    }

    // ─── Merchants ───────────────────────────────────────────────────────────

    @Transactional
    public MerchantResponse createMerchant(MerchantRequest request) {
        Merchant merchant = Merchant.builder()
                .name(request.getName())
                .type(request.getType())
                .contactNumber(request.getContactNumber())
                .city(request.getCity())
                .isActive(true)
                .build();
        merchant = merchantRepository.save(merchant);
        return mapMerchantToResponse(merchant);
    }

    public List<MerchantResponse> getMerchantsByCity(String city) {
        return merchantRepository.findByCityAndIsActive(city, true).stream()
                .map(this::mapMerchantToResponse).collect(Collectors.toList());
    }

    public List<MerchantResponse> getMerchantsByType(String type) {
        return merchantRepository.findByTypeAndIsActive(type, true).stream()
                .map(this::mapMerchantToResponse).collect(Collectors.toList());
    }

    // ─── Validation ──────────────────────────────────────────────────────────

    private void validateOrder(OrderRequest request) {
        if (request.getOrderType().equals("CLOTHING_RENT")) {
            if (request.getRentalStartDate() == null || request.getRentalEndDate() == null) {
                throw new IllegalArgumentException("Rental orders require start and end dates.");
            }
            if (request.getRentalStartDate().isAfter(request.getRentalEndDate())) {
                throw new IllegalArgumentException("Rental start must be before end date.");
            }
        }
        if (request.getOrderType().equals("GUIDE_BOOKING") && request.getGuideId() == null) {
            throw new IllegalArgumentException("Guide bookings require a guideId.");
        }
    }

    // ─── Mappers ─────────────────────────────────────────────────────────────

    private OrderResponse mapOrderToResponse(Order o) {
        return OrderResponse.builder()
                .orderId(o.getId())
                .orderType(o.getOrderType().name())
                .status(o.getStatus().name())
                .totalAmount(o.getTotalAmount())
                .merchantId(o.getMerchantId())
                .guideId(o.getGuideId())
                .deliveryAddressId(o.getDeliveryAddressId())
                .paymentId(o.getPaymentId())
                .rentalStartDate(o.getRentalStartDate())
                .rentalEndDate(o.getRentalEndDate())
                .createdAt(o.getCreatedAt())
                .build();
    }

    private MerchantResponse mapMerchantToResponse(Merchant m) {
        return MerchantResponse.builder()
                .merchantId(m.getId())
                .name(m.getName())
                .type(m.getType())
                .contactNumber(m.getContactNumber())
                .city(m.getCity())
                .rating(m.getRating())
                .isActive(m.getIsActive())
                .build();
    }
}
