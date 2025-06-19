/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.Order;
import com.carolinawings.webapp.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImplementation implements OrderService {

    private OrderRepository orderRepository;

    public OrderServiceImplementation(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    @Override
    public String createOrder(Order order) {
       orderRepository.save(order);
       return "Order with id: "+ order.getId()+" created successfully";
    }

    @Override
    public String deleteOrder(UUID id) {
        if(!orderRepository.existsById(id)) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        orderRepository.deleteById(id);
        return "Order with id: "+ id +" deleted successfully";
    }

    @Override
    public Order updateOrder(Order order, UUID id) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setOrderAmount(order.getOrderAmount());
                    existingOrder.setUser(order.getUser());
                    existingOrder.setRestaurantAssignedTo(order.getRestaurantAssignedTo());
                    existingOrder.setPickupTime(order.getPickupTime());
                    return orderRepository.save(existingOrder);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }
}
