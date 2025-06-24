/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.Restaurant;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface RestaurantService {
    List<Restaurant> getAllRestaurants();
    Optional<Restaurant> getRestaurantById(UUID id);
    String createRestaurant(Restaurant restaurant);
    String deleteRestaurant(UUID id);
    Restaurant updateRestaurant(Restaurant restaurant, UUID id);
}
