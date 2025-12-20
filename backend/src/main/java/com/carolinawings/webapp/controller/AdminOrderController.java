package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.dto.OrderResponseDTO;
import com.carolinawings.webapp.dto.PagedOrderResponseDTO;
import com.carolinawings.webapp.dto.UpdateOrderStatusRequest;
import com.carolinawings.webapp.enums.OrderStatus;
import com.carolinawings.webapp.model.Order;
import com.carolinawings.webapp.repository.OrderRepository;
import com.carolinawings.webapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    // GET /api/manager/restaurants/{restaurantId}/orders
    @GetMapping("/restaurants/{restaurantId}/orders")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_RESTAURANT_ADMIN')")
    public ResponseEntity<PagedOrderResponseDTO> getAllOrders(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) OrderStatus status
    ) {
        PagedOrderResponseDTO pagedOrderResponseDTO = orderService.getOrdersForRestaurantByManager(
                restaurantId, page, size, status);
        return new ResponseEntity<>(pagedOrderResponseDTO, HttpStatus.OK);
    }

    // PATCH /api/manager/orders/{orderId}/status
    @PatchMapping("/orders/{orderId}/status")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_RESTAURANT_ADMIN')")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestBody UpdateOrderStatusRequest status
    ) {
        OrderResponseDTO dto =
                orderService.updateOrderStatusForManager(orderId, status.getOrderStatus());

       return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
