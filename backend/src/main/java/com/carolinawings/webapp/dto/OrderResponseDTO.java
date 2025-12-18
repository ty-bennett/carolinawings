package com.carolinawings.webapp.dto;

import com.carolinawings.webapp.model.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderResponseDTO {
    private UUID orderId;
    private OrderStatus orderStatus;
    private BigDecimal subtotal;
    private BigDecimal totalTax;
    private BigDecimal totalPrice;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime pickupTime;
    private Long restaurantId;
    private String customerName;
    private String customerPhone;
    private String customerNotes;
    private OrderType orderType;
    private List<OrderItemResponse> items;
}
