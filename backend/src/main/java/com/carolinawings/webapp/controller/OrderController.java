package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.dto.OrderDTO;
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



    // Get an order by its UUID
    @GetMapping("/orders/{id}")
    public ResponseEntity<Optional<OrderDTO>> getOrderById(@PathVariable UUID id) {
        return new ResponseEntity<>(orderServiceImplementation.getOrderById(id), HttpStatus.OK);
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
