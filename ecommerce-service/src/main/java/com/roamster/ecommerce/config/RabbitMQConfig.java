package com.roamster.ecommerce.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE          = "roamster.events";
    public static final String ECOMMERCE_QUEUE   = "ecommerce.events.queue";
    public static final String DLQ               = "ecommerce.events.dlq";

    @Bean
    TopicExchange roamsterExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue ecommerceQueue() {
        return QueueBuilder.durable(ECOMMERCE_QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", DLQ)
                .build();
    }

    @Bean
    Queue ecommerceDlq() {
        return QueueBuilder.durable(DLQ).build();
    }

    /**
     * Subscribe to all clothing order events and general ecommerce events.
     */
    @Bean
    Binding ecommerceBinding(Queue ecommerceQueue, TopicExchange roamsterExchange) {
        return BindingBuilder.bind(ecommerceQueue)
                .to(roamsterExchange)
                .with("clothing.order.#");
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
