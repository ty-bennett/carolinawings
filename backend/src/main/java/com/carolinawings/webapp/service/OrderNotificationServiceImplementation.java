package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.OrderDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderNotificationServiceImplementation implements OrderNotificationService{

    private final SimpMessagingTemplate messagingTemplate;

    public OrderNotificationServiceImplementation(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyNewOrder(Long restaurantId, OrderDTO order) {
        messagingTemplate.convertAndSend("/topic/orders/" + restaurantId, order);
    }

    public void notifyOrderStatusChange(Long restaurantId, OrderDTO order) {
        messagingTemplate.convertAndSend("/topic/orders/" + restaurantId + "/status", order);
    }
}
