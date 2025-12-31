package com.carolinawings.webapp.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "orders.exchange";
    public static final String ALL_ORDERS_QUEUE = "orders.all";
    public static final String WILDCARD_ROUTING_KEY = "orders.restaurant.*";

    @Bean
    public Queue allOrdersQueue() {
        return new Queue(ALL_ORDERS_QUEUE, true);
    }

    @Bean
    public Binding allOrdersBinding(Queue allOrdersQueue, TopicExchange ordersExchange) {
        return BindingBuilder
                .bind(allOrdersQueue)
                .to(ordersExchange)
                .with(WILDCARD_ROUTING_KEY);
    }

    @Bean
    public TopicExchange ordersExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new  RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    // helper method to create new queue name for a restaurant
    public static String getQueueName(Long restaurantId) {
        return "orders.restaurant." + restaurantId;
    }

    public static String getRoutingKey(Long restaurantId) {
        return "orders.restaurant." + restaurantId;
    }
}