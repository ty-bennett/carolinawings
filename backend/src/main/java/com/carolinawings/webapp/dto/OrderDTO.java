package com.carolinawings.webapp.dto;

import com.carolinawings.webapp.model.MenuItem;
import com.carolinawings.webapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long restaurantId;
    private String pickupTime;
    private String orderAmount;
    private UserResponseDTO userAssignedTo;
    private List<Integer> listOfItems;
}
