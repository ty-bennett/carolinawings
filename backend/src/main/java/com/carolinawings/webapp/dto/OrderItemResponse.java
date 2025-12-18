package com.carolinawings.webapp.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @ToString @Getter @Setter @Builder
public class OrderItemResponse {
    private String menuItemName;
    private BigDecimal menuItemUnitPrice;
    private String menuItemDescription;
    private Integer quantity;
    private BigDecimal lineTotal;
    private List<OptionResponse> options;
}
