package com.carolinawings.webapp.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    @JsonIgnore
    private CartDTO cart;
    private MenuItemDTO menuItem;
    private Integer quantity;
    private Double price;
    private String memos;
    private List<String> sauces;
}
