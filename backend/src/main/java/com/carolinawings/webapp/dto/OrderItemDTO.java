package com.carolinawings.webapp.dto;


import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @Builder
public class OrderItemDTO {
    private Long id;
    private Long menuItemId;
    private String menuItemName;
    private String optionGroupName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal lineTotal;
    private List<OrderItemOptionDTO> options;
}
