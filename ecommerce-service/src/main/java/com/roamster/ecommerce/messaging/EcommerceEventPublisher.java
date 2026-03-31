package com.roamster.ecommerce.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EcommerceEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public static final String EXCHANGE              = "roamster.events";
    public static final String KEY_ORDER_CREATED     = "ecommerce.order.created";
    public static final String KEY_ORDER_STATUS      = "ecommerce.order.status.changed";

    public void publishOrderCreated(Long orderId, Long userId, String orderType) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", orderId);
        payload.put("userId", userId);
        payload.put("orderType", orderType);
        log.info("Publishing ecommerce.order.created: {}", payload);
        rabbitTemplate.convertAndSend(EXCHANGE, KEY_ORDER_CREATED, payload);
    }

    public void publishOrderStatusChanged(Long orderId, String newStatus, Long userId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", orderId);
        payload.put("newStatus", newStatus);
        payload.put("userId", userId);
        log.info("Publishing ecommerce.order.status.changed: {}", payload);
        rabbitTemplate.convertAndSend(EXCHANGE, KEY_ORDER_STATUS, payload);
    }
}
