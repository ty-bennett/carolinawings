package com.carolinawings.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectedOptionGroupDTO {
    private Long groupId;
    private String groupName;
    private List<String> selectedOptionNames;
}
