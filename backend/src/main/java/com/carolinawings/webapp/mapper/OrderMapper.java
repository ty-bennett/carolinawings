package com.carolinawings.webapp.mapper;

import com.carolinawings.webapp.dto.OrderResponseDTO;
import com.carolinawings.webapp.model.Order;
import com.carolinawings.webapp.dto.OrderItemDTO;
import com.carolinawings.webapp.model.OrderItem;

import java.util.ArrayList;

public final class OrderMapper {
    private OrderMapper() {}

    public static OrderResponseDTO toOrderResponseDTO(Order order) {
        if (order == null) return null;
        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .orderStatus(order.getStatus())
                .subtotal(order.getSubtotal())
                .totalTax(order.getTotalTax())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .pickupTime(order.getPickupTime())
                .restaurantId(order.getRestaurant() != null ?
                        order.getRestaurant().getId()
                        : null)
                //.customerName(order.getUser() != null ? order.getUser().getName() : null)
                //.customerPhone(order.getUser() != null ? order.getUser().getPhoneNumber() : null)
                //.customerNotes(order.getCustomerNotes())
                //.orderType(OrderType.PICKUP)
                .items(order.getItems().stream().map(OrderMapper::toOrderItemDTO).toList())
                .build();
    }

    public static OrderItemDTO toOrderItemDTO(OrderItem item) {
        if (item == null) return null;
        return OrderItemDTO.builder()
                .id(item.getId())
                .menuItemId(item.getMenuItemId())
                .menuItemName(item.getMenuItemName())
                .unitPrice(item.getMenuItemPrice())
                .quantity(item.getQuantity())
                .lineTotal(item.getTotalPrice())
                .options(new ArrayList<>())
                .build();
    }
}
