package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.dto.OrderCreateRequest;
import com.carolinawings.webapp.dto.OrderDTO;
import com.carolinawings.webapp.dto.OrderResponseDTO;
import com.carolinawings.webapp.repository.OrderRepository;
import com.carolinawings.webapp.repository.RestaurantRepository;
import com.carolinawings.webapp.service.OrderServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {

    private final OrderServiceImplementation orderServiceImplementation;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;

    public AdminOrderController(OrderServiceImplementation orderServiceImplementation, RestaurantRepository restaurantRepository, OrderRepository orderRepository) {
        this.orderServiceImplementation = orderServiceImplementation;
        this.restaurantRepository = restaurantRepository;
        this.orderRepository = orderRepository;
    }

    // Get an order by its UUID
    @GetMapping("/orders/{orderId}")
    @PreAuthorize("@securityService.canViewOrder(#orderId)")
    public ResponseEntity<Optional<OrderDTO>> getOrderById(@PathVariable UUID orderId) {
        return new ResponseEntity<>(orderServiceImplementation.getOrderById(orderId), HttpStatus.OK);
    }

    // Delete an order using its UUID
    @DeleteMapping("/orders/{orderId}")
    @PreAuthorize("@securityService.canManageOrder(#orderId)")
    public ResponseEntity<OrderDTO> deleteOrderById(@PathVariable UUID orderId) {
        OrderDTO deletedOrder = orderServiceImplementation.deleteOrderById(orderId);
        return new ResponseEntity<>(deletedOrder, HttpStatus.OK);
    }

    // Update info in an order
    @PutMapping("/orders/{orderId}")
    @PreAuthorize("@securityService.canManageOrder(#orderId)")
    public ResponseEntity<OrderDTO> updateOrder(@Valid @RequestBody OrderDTO orderDTO,
                                                @PathVariable UUID orderId) {
        OrderDTO savedOrderDTO = orderServiceImplementation.updateOrder(orderDTO, orderId);
        return new ResponseEntity<>(savedOrderDTO, HttpStatus.OK);
    }


}
