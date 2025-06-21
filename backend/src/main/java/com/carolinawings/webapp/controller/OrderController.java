/*
Ty Bennett
*/
package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.model.Order;
import com.carolinawings.webapp.service.OrderServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class OrderController {
    private final OrderServiceImplementation orderServiceImplementation;

    public OrderController(OrderServiceImplementation orderServiceImplementation) {
        this.orderServiceImplementation= orderServiceImplementation;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return new ResponseEntity<>(orderServiceImplementation.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Optional<Order>> getOrderById(@PathVariable UUID id) {
        return new ResponseEntity<>(orderServiceImplementation.getOrderById(id), HttpStatus.OK);
    }

    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        orderServiceImplementation.createOrder(order);
        return new ResponseEntity<>("Menu item created successfully " + order, HttpStatus.CREATED);
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable UUID id) {
        try {
            return new ResponseEntity<>(orderServiceImplementation.deleteOrder(id), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
    @PutMapping("/orders/{id}")
    public ResponseEntity<String> updateOrder(@RequestBody Order order,
                                                 @PathVariable UUID id)
    {
        try {
            orderServiceImplementation.updateOrder(order, id);
            return new ResponseEntity<>("order edited with existing id" + id, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

}

