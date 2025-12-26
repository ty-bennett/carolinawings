package com.carolinawings.webapp.dto;

import com.carolinawings.webapp.enums.RestaurantStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {
    private Long id;
    private String name;
    private String address;
    private Set<UUID> restaurantAdmin;
    private Set<MenuDTO> menus;
    private RestaurantStatus status;
    private boolean acceptingOrders;
    private Integer estimatedPickupMinutes;
    private List<RestaurantHoursDTO> hours;
}
