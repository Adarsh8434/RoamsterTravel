package com.roamster.ecommerce.controller;

import com.roamster.ecommerce.dto.EcommerceDTOs.*;
import com.roamster.ecommerce.service.EcommerceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ecommerce")
@RequiredArgsConstructor
@Tag(name = "E-commerce / Booking Service", description = "Clothing rent/buy orders, guide bookings, merchant management")
public class EcommerceController {

    private final EcommerceService ecommerceService;

    // ─── Orders ──────────────────────────────────────────────────────────────

    @PostMapping("/orders")
    @Operation(summary = "Create a new order (clothing rent/buy or guide booking)")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ecommerceService.createOrder(request));
    }

    @GetMapping("/orders/{orderId}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(ecommerceService.getOrder(orderId));
    }

    @PatchMapping("/orders/{orderId}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        return ResponseEntity.ok(ecommerceService.updateStatus(orderId, status));
    }

    @GetMapping("/orders/user/{userId}")
    @Operation(summary = "Get all orders for a user")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(ecommerceService.getUserOrders(userId));
    }

    @GetMapping("/orders/trip/{tripId}")
    @Operation(summary = "Get all orders for a trip")
    public ResponseEntity<List<OrderResponse>> getTripOrders(@PathVariable Long tripId) {
        return ResponseEntity.ok(ecommerceService.getTripOrders(tripId));
    }

    // ─── Merchants ───────────────────────────────────────────────────────────

    @PostMapping("/merchants")
    @Operation(summary = "Register a new merchant")
    public ResponseEntity<MerchantResponse> createMerchant(@Valid @RequestBody MerchantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ecommerceService.createMerchant(request));
    }

    @GetMapping("/merchants")
    @Operation(summary = "Search merchants by city or type")
    public ResponseEntity<List<MerchantResponse>> getMerchants(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String type) {
        if (city != null) return ResponseEntity.ok(ecommerceService.getMerchantsByCity(city));
        if (type != null) return ResponseEntity.ok(ecommerceService.getMerchantsByType(type));
        return ResponseEntity.badRequest().build();
    }
}
