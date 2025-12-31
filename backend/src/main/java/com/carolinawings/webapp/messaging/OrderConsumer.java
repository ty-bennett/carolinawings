package com.carolinawings.webapp.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderConsumer {


    @RabbitListener(queues = RabbitMQConfig.ALL_ORDERS_QUEUE)
    public void receiveOrder(OrderMessage orderMessage) {
        log.info("========== NEW ORDER RECEIVED ==========");
        log.info("Order ID: {}", orderMessage.getOrderId());
        log.info("Restaurant: {}", orderMessage.getRestaurantName());
        log.info("Customer: {} - {}", orderMessage.getCustomerName(), orderMessage.getCustomerPhone());
        log.info("Pickup Time: {}", orderMessage.getPickupTime());
        log.info("-----------------------------------------");

        for (OrderMessage.OrderItemMessage item : orderMessage.getItems()) {
            log.info("  {} x {} - ${}", item.getQuantity(), item.getName(), item.getPrice());
            if (item.getOptions() != null && !item.getOptions().isEmpty()) {
                log.info("    Options: {}", String.join(", ", item.getOptions()));
            }
        }

        log.info("-----------------------------------------");
        log.info("TOTAL: ${}", orderMessage.getTotalPrice());
        if (orderMessage.getCustomerNotes() != null) {
            log.info("Notes: {}", orderMessage.getCustomerNotes());
        }
        log.info("=========================================");

        //TODO: Send order to printer at restaurant
    }
}
