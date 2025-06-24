/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.service;

import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Order;
import com.carolinawings.webapp.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImplementation(OrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders()
    {
        List<Order> orders = orderRepository.findAll();
        if(orders.isEmpty())
            throw new APIException("No orders present");
        return orders;
    }

    @Override
    public String createOrder(Order order) {
        orderRepository.save(order);
        return "Order with id " + order.getId() + " added successfully";
    }

    @Override
    public Optional<Order> getOrderById (UUID id) {
        return orderRepository.findById(id);
    }

    @Override
    public String deleteOrder(UUID id) {
        Order o = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", id));
        orderRepository.delete(o);
        return "Order with id " + id + " deleted successfully";
    }

    @Override
    public Order updateOrder(Order order, UUID id) {
        Order savedOrder = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", id));
        order.setId(id);
        return orderRepository.save(savedOrder);
    }
}
