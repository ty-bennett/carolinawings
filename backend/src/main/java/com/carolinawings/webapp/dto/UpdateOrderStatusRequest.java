package com.carolinawings.webapp.dto;

import com.carolinawings.webapp.enums.OrderStatus;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Getter
@Setter @ToString
public class UpdateOrderStatusRequest {
    private OrderStatus orderStatus;
}
