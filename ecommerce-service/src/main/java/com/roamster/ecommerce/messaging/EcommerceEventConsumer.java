package com.roamster.ecommerce.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Consumes order events originating from clothing-service.
 * Keeps ecommerce order state in sync with clothing order lifecycle.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EcommerceEventConsumer {

    @RabbitListener(queues = "ecommerce.events.queue")
    public void handleClothingOrderPlaced(Map<String, Object> payload) {
        log.info("Received clothing.order.placed event: {}", payload);
        Long orderId = Long.valueOf(payload.get("orderId").toString());
        Long userId  = Long.valueOf(payload.get("userId").toString());
        String type  = payload.get("orderType").toString();

        // Sync order into ecommerce tracking, trigger inventory check, notify admin, etc.
        log.info("Syncing clothing order orderId={} userId={} type={} into ecommerce layer", orderId, userId, type);
    }

    @RabbitListener(queues = "ecommerce.events.queue")
    public void handleOrderStatusUpdated(Map<String, Object> payload) {
        log.info("Received order.status.updated event: {}", payload);
        // Trigger notification, update fulfilment pipeline, etc.
    }
}
