package com.carolinawings.webapp.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "orders.exchange";

    // We'll create queues dynamically per restaurant, but here's a default
    public static final String DEFAULT_QUEUE = "orders.queue";
    public static final String DEFAULT_ROUTING_KEY = "orders.default";

    @Bean
    public DirectExchange ordersExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue defaultOrdersQueue() {
        return new Queue(DEFAULT_QUEUE, true); // durable = true
    }

    @Bean
    public Binding defaultBinding(Queue defaultOrdersQueue, DirectExchange ordersExchange) {
        return BindingBuilder
                .bind(defaultOrdersQueue)
                .to(ordersExchange)
                .with(DEFAULT_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}