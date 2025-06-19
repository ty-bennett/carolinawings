/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Service
public interface OrderService {
    List<Order> getAllOrders();
    Optional<Order> getOrderById(UUID id);
    String createOrder(Order order);
    String deleteOrder(UUID id);
    Order updateOrder(Order order, UUID id);
}
