package com.carolinawings.webapp.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @Builder
public class OrderItemOptionDTO {
    private Long optionId;
    private String optionGroupName;
    private String optionName;
    private BigDecimal extraPrice;
}
