/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.OrderDTO;
import com.carolinawings.webapp.dto.OrderResponse;

import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    OrderResponse getAllOrdersByRestaurantPaged(Integer pageNumber, Integer pageSize, Long restaurantId);
    Optional<OrderDTO> getOrderById(UUID id);
    OrderDTO createOrderByRestaurant(Long id, OrderDTO order);
    OrderDTO deleteOrderById(UUID id);
    OrderDTO updateOrder(OrderDTO order, UUID id);
}
