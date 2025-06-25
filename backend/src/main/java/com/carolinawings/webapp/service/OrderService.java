/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.OrderDTO;
import com.carolinawings.webapp.dto.OrderResponse;
import com.carolinawings.webapp.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    OrderResponse getAllOrders();
    OrderResponse getAllOrdersPaged(Integer page, Integer pageSize);
    Optional<OrderDTO> getOrderById(UUID id);
    OrderDTO createOrder(OrderDTO order);
    OrderDTO deleteOrderById(UUID id);
    OrderDTO updateOrder(OrderDTO order, UUID id);
}
