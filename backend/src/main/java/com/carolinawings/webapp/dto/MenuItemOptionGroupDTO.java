package com.carolinawings.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemOptionGroupDTO {
    private String id;
    private String optionType;
    private String required;
    private String minChoices;
    private String maxChoices;
    private MenuItemDTO menuItem;
    private OptionGroupDTO optionGroup;
}
