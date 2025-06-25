package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.config.ApplicationConstants;
import com.carolinawings.webapp.dto.OrderDTO;
import com.carolinawings.webapp.dto.OrderResponse;
import com.carolinawings.webapp.service.OrderServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class OrderController {

    private final OrderServiceImplementation orderServiceImplementation;

    public OrderController(OrderServiceImplementation orderServiceImplementation) {
        this.orderServiceImplementation = orderServiceImplementation;
    }

    // Get all orders
    @GetMapping("/orders/all")
    public ResponseEntity<OrderResponse> getOrders() {
        return new ResponseEntity<>(orderServiceImplementation.getAllOrders(), HttpStatus.OK);
    }

    // Get all orders with pagination
    @GetMapping("/orders")
    public ResponseEntity<OrderResponse> getOrders(
            @RequestParam(name = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize) {
        return new ResponseEntity<>(orderServiceImplementation.getAllOrdersPaged(pageNumber, pageSize), HttpStatus.OK);
    }

    // Get an order by its UUID
    @GetMapping("/orders/{id}")
    public ResponseEntity<Optional<OrderDTO>> getOrderById(@PathVariable UUID id) {
        return new ResponseEntity<>(orderServiceImplementation.getOrderById(id), HttpStatus.OK);
    }

    // Create an order with a valid object in request
    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO savedOrderDTO = orderServiceImplementation.createOrder(orderDTO);
        return new ResponseEntity<>(savedOrderDTO, HttpStatus.CREATED);
    }

    // Delete an order using its UUID
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<OrderDTO> deleteOrderById(@PathVariable UUID id) {
        OrderDTO deletedOrder = orderServiceImplementation.deleteOrderById(id);
        return new ResponseEntity<>(deletedOrder, HttpStatus.OK);
    }

    // Update info in an order
    @PutMapping("/orders/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@Valid @RequestBody OrderDTO orderDTO,
                                                @PathVariable UUID id) {
        OrderDTO savedOrderDTO = orderServiceImplementation.updateOrder(orderDTO, id);
        return new ResponseEntity<>(savedOrderDTO, HttpStatus.OK);
    }
}
