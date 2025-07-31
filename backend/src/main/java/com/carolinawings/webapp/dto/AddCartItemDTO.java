package com.carolinawings.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCartItemDTO {
    private Long menuItemId;
    private Long cartId;
    private Integer quantity;
    private String memos;
    private List<Long> selectedSauceOptionIds;
    private List<Long> selectedDressingIds;
}
