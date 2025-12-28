package com.carolinawings.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemOptionDTO {
    private String id;
    private String optionGroupName;
    private String optionName;
    private BigDecimal price;
}
