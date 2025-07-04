package com.carolinawings.webapp.dto;

import java.util.Set;
import java.util.UUID;

public class RestaurantDTO {
    private String name;
    private String address;
    private Set<UUID> restaurantAdmin;
    private Set<Long> menus;
}
