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
    private int minChoices;
    private int maxChoices;
    private OptionGroupDTO optionGroup;
    private boolean required;
}
