package com.carolinawings.webapp.dto;

import com.carolinawings.webapp.model.MenuItemOptionGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String price;
    private String category;
    private Boolean enabled;
    private List<MenuItemOptionGroup> optionGroups;
}
