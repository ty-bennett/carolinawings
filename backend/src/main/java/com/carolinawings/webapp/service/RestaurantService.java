/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.Restaurant;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RestaurantService {
    List<Restaurant> getAllRestaurants();
    Optional<Restaurant> getRestaurantById(Long id);
    String createRestaurant(Restaurant restaurant);
    String deleteRestaurant(Long id);
    Restaurant updateRestaurant(Restaurant restaurant, Long id);
}
