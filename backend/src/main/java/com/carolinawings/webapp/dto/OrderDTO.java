package com.carolinawings.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String restaurant;
    private String pickupTime;
    private String orderAmount;
    private UUID userAssignedTo;
    private List<Integer> listOfItems;
}
