package com.carolinawings.webapp.service;

import com.carolinawings.webapp.messaging.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QueueServiceImplementation implements QueueService {

    private final RabbitAdmin rabbitAdmin;
    private final TopicExchange ordersExchange;

    public QueueServiceImplementation(RabbitAdmin rabbitAdmin, TopicExchange ordersExchange) {
        this.rabbitAdmin = rabbitAdmin;
        this.ordersExchange = ordersExchange;
    }

    public void createQueueForRestaurant(Long restaurantId) {
        String queueName = RabbitMQConfig.getQueueName(restaurantId);
        String routingKey = RabbitMQConfig.getRoutingKey(restaurantId);

        Queue queue = new Queue(queueName, true);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(
                BindingBuilder.bind(queue).to(ordersExchange).with(routingKey)
        );

        log.info("Created queue {} with routing key {}", queueName, routingKey);
    }

    public void deleteQueueForRestaurant(Long restaurantId) {
        String queueName = RabbitMQConfig.getQueueName(restaurantId);
        rabbitAdmin.deleteQueue(queueName);
        log.info("Deleted queue {}", queueName);
    }
}