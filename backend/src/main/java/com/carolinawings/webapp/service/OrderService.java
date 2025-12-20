/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.OrderCreateRequest;
import com.carolinawings.webapp.dto.OrderDTO;
import com.carolinawings.webapp.dto.OrderResponseDTO;
import com.carolinawings.webapp.dto.PagedOrderResponseDTO;
import com.carolinawings.webapp.enums.OrderStatus;

import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    OrderResponseDTO getAllOrdersByRestaurantPaged(Integer pageNumber, Integer pageSize, Long restaurantId);
    Optional<OrderDTO> getOrderById(UUID id);
    //OrderDTO createOrderByRestaurant(Long id, OrderDTO order);
    OrderDTO deleteOrderById(UUID id);
    OrderDTO updateOrder(OrderDTO order, UUID id);
    OrderResponseDTO createOrderFromCart(OrderCreateRequest request);

    PagedOrderResponseDTO getOrdersForRestaurantByManager(
            Long restaurantId,
            Integer page,
            Integer pageSize,
            OrderStatus statusFilter
    );

    OrderResponseDTO updateOrderStatusForManager(UUID orderId, OrderStatus orderStatus);
}
