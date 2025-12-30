package com.carolinawings.webapp.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderToKitchen(OrderMessage orderMessage) {
        String routingKey = "orders.restaurant." + orderMessage.getRestaurantId();

        log.info("Sending order to Kitchen: {} with routing key: {}",
                orderMessage.getOrderId(),  routingKey);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                routingKey,
                orderMessage
        );

        log.info("Order {} sent successfully", orderMessage.getOrderId());
    }
}
