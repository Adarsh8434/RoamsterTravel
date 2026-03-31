package com.roamster.clothing.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClothingEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public static final String EXCHANGE       = "roamster.events";
    public static final String KEY_REC_GEN    = "clothing.recommendation.generated";
    public static final String KEY_ORDER_PLACED = "clothing.order.placed";
    public static final String KEY_ORDER_STATUS = "clothing.order.status.updated";

    public void publishRecommendationGenerated(Long recommendationId, Long tripId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("recommendationId", recommendationId);
        payload.put("tripId", tripId);
        payload.put("category", "CLOTHING");

        log.info("Publishing recommendation.generated event: {}", payload);
        rabbitTemplate.convertAndSend(EXCHANGE, KEY_REC_GEN, payload);
    }

    public void publishOrderPlaced(Long orderId, Long userId, String orderType) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", orderId);
        payload.put("userId", userId);
        payload.put("orderType", orderType);
        payload.put("service", "CLOTHING");

        log.info("Publishing order.placed event: {}", payload);
        rabbitTemplate.convertAndSend(EXCHANGE, KEY_ORDER_PLACED, payload);
    }

    public void publishOrderStatusUpdated(Long orderId, String newStatus) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", orderId);
        payload.put("newStatus", newStatus);
        payload.put("service", "CLOTHING");

        log.info("Publishing order.status.updated event: {}", payload);
        rabbitTemplate.convertAndSend(EXCHANGE, KEY_ORDER_STATUS, payload);
    }
}
