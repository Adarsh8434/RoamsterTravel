package com.roamster.food.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class FoodEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public static final String EXCHANGE    = "roamster.events";
    public static final String KEY_REC_GEN = "food.recommendation.generated";

    public void publishRecommendationGenerated(Long recommendationId, Long tripId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("recommendationId", recommendationId);
        payload.put("tripId", tripId);
        payload.put("category", "FOOD");
        log.info("Publishing food recommendation event: {}", payload);
        rabbitTemplate.convertAndSend(EXCHANGE, KEY_REC_GEN, payload);
    }
}
