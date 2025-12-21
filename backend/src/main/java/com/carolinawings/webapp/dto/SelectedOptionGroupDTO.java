package com.carolinawings.webapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectedOptionGroupDTO {
    private Long optionGroupId;
    private List<Long> selectedOptionIds;
}
