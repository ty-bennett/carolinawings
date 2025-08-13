package com.carolinawings.webapp.dto;

import com.carolinawings.webapp.model.MenuItemOptionGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private String category;
    private Boolean enabled;
    @JsonIgnore
    private List<MenuItemOptionGroupDTO> optionGroups;
}
