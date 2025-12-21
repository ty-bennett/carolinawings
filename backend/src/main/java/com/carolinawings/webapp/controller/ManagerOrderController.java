package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.dto.OrderResponseDTO;
import com.carolinawings.webapp.dto.PagedOrderResponseDTO;
import com.carolinawings.webapp.dto.UpdateOrderStatusRequest;
import com.carolinawings.webapp.enums.OrderStatus;
import com.carolinawings.webapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerOrderController {

    private final OrderService orderService;

    // GET /api/manager/restaurants/{restaurantId}/orders
    @GetMapping("/restaurants/{restaurantId}/orders")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'RESTAURANT_ADMIN')")
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
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'RESTAURANT_ADMIN')")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestBody UpdateOrderStatusRequest orderStatus
    ) {
        OrderResponseDTO dto =
                orderService.updateOrderStatusForManager(orderId, orderStatus.getOrderStatus());

       return ResponseEntity.ok(dto);
    }
}
