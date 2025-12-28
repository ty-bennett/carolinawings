package com.carolinawings.webapp.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItemDTO {
    private Long cartItemId;
    private MenuItemDTO menuItem;
    private Integer quantity;
    private BigDecimal price;
    private String memos;
    private List<CartItemOptionDTO> options;
}
