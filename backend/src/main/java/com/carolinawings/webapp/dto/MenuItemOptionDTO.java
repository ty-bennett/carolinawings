package com.carolinawings.webapp.dto;

import com.carolinawings.webapp.dto.OptionGroupDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemOptionDTO {
    private String id;
    private String name;
    private boolean defaultSelected;
}
