/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.RestaurantDTO;
import com.carolinawings.webapp.dto.RestaurantHoursDTO;
import com.carolinawings.webapp.dto.RestaurantResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantService {
    RestaurantResponse getAllRestaurants();
    RestaurantResponse getAllRestaurantsPaged(Integer page, Integer pageSize);
    RestaurantDTO getRestaurantById(Long id);
    RestaurantDTO createRestaurant(RestaurantDTO restaurant);
    RestaurantDTO deleteRestaurant(Long id);
    RestaurantDTO updateRestaurant(RestaurantDTO restaurantDTO, Long id);
    RestaurantDTO setAcceptingOrders(Long restaurantId, boolean accepting);
    RestaurantDTO setEstimatedPickupMinutes(Long restaurantId, Integer minutes);
    List<RestaurantHoursDTO> getRestaurantHours(Long restaurantId);
    List<RestaurantHoursDTO> updateRestaurantHours(Long restaurantId, List<RestaurantHoursDTO> hours);
}
