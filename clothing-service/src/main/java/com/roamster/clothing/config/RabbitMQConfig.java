package com.roamster.clothing.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE          = "roamster.events";
    public static final String CLOTHING_QUEUE    = "clothing.events.queue";
    public static final String DLQ               = "clothing.events.dlq";

    @Bean
    TopicExchange roamsterExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue clothingQueue() {
        return QueueBuilder.durable(CLOTHING_QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", DLQ)
                .build();
    }

    @Bean
    Queue clothingDlq() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    Binding clothingBinding(Queue clothingQueue, TopicExchange roamsterExchange) {
        return BindingBuilder.bind(clothingQueue)
                .to(roamsterExchange)
                .with("clothing.#");
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
