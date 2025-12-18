package com.carolinawings.webapp.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor @ToString @Builder @Getter @Setter
public class OptionResponse {
    private String optionName;
    private BigDecimal extraPrice;
    private String groupName;
}
