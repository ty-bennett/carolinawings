package com.carolinawings.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCartItemDTO {
    private Long menuItemId;
    private Integer quantity;
    private String memos;
    private String dressing;
    private String sauces;
}
