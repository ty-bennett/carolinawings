package com.carolinawings.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private CartDTO cart;
    private MenuItemDTO menuItem;
    private Integer quantity;
    private Double price;
    private String memos;
    private String sauces;
    private String dressing;
}
