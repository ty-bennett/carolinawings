package com.carolinawings.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String restaurant;
    private String pickupTime;
    private String orderAmount;
    private String userAssignedTo;
    private List<Integer> listOfItems;
}
