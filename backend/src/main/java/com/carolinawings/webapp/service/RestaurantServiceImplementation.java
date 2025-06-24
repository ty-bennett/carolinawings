package com.carolinawings.webapp.service;

import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Restaurant;
import com.carolinawings.webapp.repository.RestaurantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RestaurantServiceImplementation implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImplementation(RestaurantRepository restaurantRepository)
    {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<Restaurant> getAllRestaurants()
    {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        if(restaurants.isEmpty())
            throw new APIException("No restaurants present");
        return restaurants;
    }

    @Override
    public String createRestaurant(Restaurant restaurant) {
        Restaurant savedRestaurant = restaurantRepository.findByName(restaurant.getName());
        if(savedRestaurant != null)
            throw new APIException("Restaurant with the name " + restaurant.getName() + " already exists");
        restaurantRepository.save(restaurant);
        return "Restaurant with id " + restaurant.getId() + " added successfully";
    }

    @Override
    public Optional<Restaurant> getRestaurantById(UUID id) {
        return restaurantRepository.findById(id);
    }

    @Override
    public String deleteRestaurant(UUID id) {
        Restaurant r = restaurantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", id));
        restaurantRepository.delete(r);
        return "Restaurant with id " + id + " deleted successfully";
    }

    @Override
    public Restaurant updateRestaurant (Restaurant restaurant, UUID id) {
        Restaurant savedRestaurant = restaurantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", id));
        restaurant.setId(id);
        return restaurantRepository.save(savedRestaurant);
    }
}
