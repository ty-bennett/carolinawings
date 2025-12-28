package com.carolinawings.webapp.dto;

import com.carolinawings.webapp.enums.OrderType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OrderCreateRequest {
    // to get cart id to associate it with
    @NotNull
    private Long cartId;
    // which restaurant the order is going to
    @NotNull
    private Long restaurantId;
    // who ordered the food
    @NotNull
    private String customerName;
    // their phone # for contact purposes
    @NotNull
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$")
    private String customerPhone;
    // any notes/memos they want to add
    private String customerNotes;
    // an enum of type (only 1)
    @NotNull
    private OrderType orderType = OrderType.PICKUP;
    // a string to be able to parse a pickup time (default when creating an order is 15 minutes from when req is made)
    private String requestedPickupTime;
}
