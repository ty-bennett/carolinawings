/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.RestaurantDTO;
import com.carolinawings.webapp.dto.RestaurantResponse;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Restaurant;
import com.carolinawings.webapp.repository.RestaurantRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RestaurantServiceImplementation implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    private ModelMapper modelMapper;

    public RestaurantServiceImplementation(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public RestaurantResponse getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        if (restaurants.isEmpty())
            throw new APIException("No restaurants present");

        List<RestaurantDTO> restaurantDTOS = restaurants.stream()
                .map(r -> modelMapper.map(r, RestaurantDTO.class))
                .toList();

        return new RestaurantResponse(restaurantDTOS);
    }

    @Override
    public RestaurantResponse getAllRestaurantsPaged(Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Restaurant> restaurants = restaurantRepository.findAll(pageDetails);
        List<Restaurant> pagedContent = restaurants.getContent();

        if (pagedContent.isEmpty())
            throw new APIException("No restaurants present");

        List<RestaurantDTO> restaurantDTOS = pagedContent.stream()
                .map(r -> modelMapper.map(r, RestaurantDTO.class))
                .toList();

        RestaurantResponse response = new RestaurantResponse();
        response.setContent(restaurantDTOS);
        response.setPageNumber(restaurants.getNumber());
        response.setPageSize(restaurants.getSize());
        response.setTotalElements(restaurants.getTotalElements());
        response.setTotalPages(restaurants.getTotalPages());
        response.setLastPage(restaurants.isLast());

        return response;
    }

    @Override
    public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = modelMapper.map(restaurantDTO, Restaurant.class);
        Restaurant existing = restaurantRepository.findByName(restaurant.getName());

        if (existing != null)
            throw new APIException("Restaurant with the name " + restaurant.getName() + " already exists");

        Restaurant saved = restaurantRepository.save(restaurant);
        return modelMapper.map(saved, RestaurantDTO.class);
    }

    @Override
    public Optional<RestaurantDTO> getRestaurantById(UUID id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        return restaurant.map(r -> modelMapper.map(r, RestaurantDTO.class));
    }

    @Override
    public RestaurantDTO deleteRestaurant(UUID id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", id));
        restaurantRepository.delete(restaurant);
        return modelMapper.map(restaurant, RestaurantDTO.class);
    }

    @Override
    public RestaurantDTO updateRestaurant(RestaurantDTO restaurantDTO, UUID id) {
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", id));

        Restaurant restaurant = modelMapper.map(restaurantDTO, Restaurant.class);
        restaurant.setId(id);
        Restaurant saved = restaurantRepository.save(restaurant);
        return modelMapper.map(saved, RestaurantDTO.class);
    }
}
