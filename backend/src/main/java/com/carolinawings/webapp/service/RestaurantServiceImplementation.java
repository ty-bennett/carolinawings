package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.Restaurant;
import com.carolinawings.webapp.repository.RestaurantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

public class RestaurantServiceImplementation implements RestaurantService {

    private RestaurantRepository restaurantRepository;

    public RestaurantServiceImplementation(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }

    @Override
    public String createRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
        return "Restaurant with id" + restaurant.getId() + " created";
    }

    @Override
    public String deleteRestaurant(Long id) {
       if (!restaurantRepository.existsById(id)) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant with id" + id + " not found");
       }
       restaurantRepository.deleteById(id);
       return "Restaurant with id" + id + " deleted";
    }

    @Override
    public Restaurant updateRestaurant(Restaurant restaurant, Long id) {
        return restaurantRepository.findById(id)
                .map(existingRestaurant -> {
                    existingRestaurant.setName(restaurant.getName());
                    existingRestaurant.setAddress(restaurant.getAddress());
                    existingRestaurant.setRestaurantAdmin(restaurant.getRestaurantAdmin());
                    return restaurantRepository.save(existingRestaurant);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant with id" + id + " not found"));
    }
}
