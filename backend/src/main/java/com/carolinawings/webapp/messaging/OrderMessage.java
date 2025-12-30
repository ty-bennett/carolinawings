package com.carolinawings.webapp.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage implements Serializable {
    private UUID orderId;
    private Long restaurantId;
    private String restaurantName;
    private String customerName;
    private String customerPhone;
    private String customerNotes;
    private OffsetDateTime pickupTime;
    private BigDecimal totalPrice;
    private List<OrderItemMessage> items;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemMessage implements Serializable {
        private String name;
        private Integer quantity;
        private BigDecimal price;
        private List<String> options;
    }
}
