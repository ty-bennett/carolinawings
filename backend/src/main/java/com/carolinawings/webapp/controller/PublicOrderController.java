package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.dto.OrderCreateRequest;
import com.carolinawings.webapp.dto.OrderDTO;
import com.carolinawings.webapp.dto.OrderResponseDTO;
import com.carolinawings.webapp.service.OrderServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PublicOrderController {

    private final OrderServiceImplementation orderServiceImplementation;

    public PublicOrderController(OrderServiceImplementation orderServiceImplementation) {
        this.orderServiceImplementation = orderServiceImplementation;
    }

    @PostMapping("/orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponseDTO> createOrderFromCart(@Valid @RequestBody OrderCreateRequest request) {
        return new ResponseEntity<>(orderServiceImplementation.createOrderFromCart(request), HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}")
    @PreAuthorize("@securityService.canViewOrder(#orderId)")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable UUID orderId) {
        return new ResponseEntity<>(orderServiceImplementation.getOrderById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)), HttpStatus.OK);
    }

    @PatchMapping("/orders/{orderId}/cancel")
    @PreAuthorize("@securityService.canCancelOrder(#orderId)")
    public ResponseEntity<OrderDTO> cancelOrderById(@PathVariable UUID orderId) {
        return new ResponseEntity<>(orderServiceImplementation.cancelOrder(orderId), HttpStatus.OK);
    }
}
