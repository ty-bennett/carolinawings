package com.carolinawings.webapp.mapper;

import com.carolinawings.webapp.dto.OrderItemOptionDTO;
import com.carolinawings.webapp.dto.OrderResponseDTO;
import com.carolinawings.webapp.enums.OrderType;
import com.carolinawings.webapp.model.Order;
import com.carolinawings.webapp.dto.OrderItemDTO;
import com.carolinawings.webapp.model.OrderItem;
import com.carolinawings.webapp.model.OrderItemOption;

public class OrderMapper {
    public OrderMapper() {}

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
                .restaurantName(order.getRestaurant().getName())
                .customerName(order.getUser() != null ? order.getUser().getFullName() : null)
                .customerPhone(order.getUser() != null ? order.getUser().getPhoneNumber() : null)
                .customerNotes(order.getCustomerNotes())
                .orderType(OrderType.PICKUP)
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
                .options(item.getOptions().stream().map(OrderMapper::toOrderItemOptionDTO).toList())
                .build();
    }

    public static OrderItemOptionDTO toOrderItemOptionDTO(OrderItemOption option) {
        if (option == null) return null;
        return OrderItemOptionDTO.builder()
                .optionId(option.getId())
                .optionName(option.getOptionName())
                .extraPrice(option.getExtraPrice())
                .optionGroupName(option.getGroupName())
                .build();
    }
}
