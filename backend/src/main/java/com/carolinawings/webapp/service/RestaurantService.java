/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.RestaurantDTO;
import com.carolinawings.webapp.dto.RestaurantResponse;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantService {
    RestaurantResponse getAllRestaurants();
    RestaurantResponse getAllRestaurantsPaged(Integer page, Integer pageSize);
    Optional<RestaurantDTO> getRestaurantById(UUID id);
    RestaurantDTO createRestaurant(RestaurantDTO restaurant);
    RestaurantDTO deleteRestaurant(UUID id);
    RestaurantDTO updateRestaurant(RestaurantDTO restaurant, UUID id);
}
