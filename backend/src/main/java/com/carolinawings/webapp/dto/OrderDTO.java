package com.carolinawings.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private UUID orderId;
    private String status;  // Maps from OrderStatus enum
    private BigDecimal subtotal;
    private BigDecimal totalTax;
    private BigDecimal totalPrice;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime pickupTime;
    private String customerName;
    private String customerPhone;
    private String customerNotes;
    private String orderType;
    private List<OrderItemDTO> items;

    // For nested objects, use IDs or simplified DTOs
    private Long restaurantId;
    private String restaurantName;
}