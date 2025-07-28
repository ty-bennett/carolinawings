package com.carolinawings.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO {
    private Long id;
    private String name;
    private String description;
    private List<MenuItemDTO> menuItemsList = new ArrayList<>();
    private Long restaurantId;
    private Boolean isPrimary = false;
}
