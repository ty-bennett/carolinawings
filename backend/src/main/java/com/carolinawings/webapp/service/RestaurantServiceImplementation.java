/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.MenuDTO;
import com.carolinawings.webapp.dto.RestaurantDTO;
import com.carolinawings.webapp.dto.RestaurantResponse;
import com.carolinawings.webapp.enums.RoleName;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Menu;
import com.carolinawings.webapp.model.Restaurant;
import com.carolinawings.webapp.model.User;
import com.carolinawings.webapp.repository.RestaurantRepository;
import com.carolinawings.webapp.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImplementation implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthUtil authUtil;

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
        User currentUser = authUtil.loggedInUser();

        Page<Restaurant> restaurants = Page.empty();

        if(currentUser.getRoles().stream().anyMatch(role -> role.getName() == RoleName.ADMIN)) {
            restaurants = restaurantRepository.findAll(pageDetails);
        } else if(currentUser.getRoles().stream().anyMatch(
                role -> role.getName() == RoleName.RESTAURANT_ADMIN || role.getName() == RoleName.MANAGER)) {
            restaurants = restaurantRepository.findAllRestaurantsByUserId(currentUser.getId(), pageDetails);
        } else {
            throw new APIException("User does not have permissions to view restaurants");
        }

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
        Restaurant existing = restaurantRepository.findByName(restaurant.getName()).orElseThrow(() -> new ResourceNotFoundException("Restaurant", "name", restaurant.getName()));

        Restaurant saved = restaurantRepository.save(restaurant);
        return modelMapper.map(saved, RestaurantDTO.class);
    }

    @Override
    public Optional<RestaurantDTO> getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantID", id));
        RestaurantDTO restaurantDTO = modelMapper.map(restaurant, RestaurantDTO.class);
        Set<UUID> restaurantAdminIds = restaurant.getRestaurantAdmin()
                .stream().map(User::getId).collect(Collectors.toSet());
        restaurantDTO.setRestaurantAdmin(restaurantAdminIds);
        return Optional.of(restaurantDTO);

    }

    @Override
    public RestaurantDTO deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", id));
        restaurantRepository.delete(restaurant);
        return modelMapper.map(restaurant, RestaurantDTO.class);
    }

    @Override
    public RestaurantDTO updateRestaurant(RestaurantDTO restaurantDTO, Long id) {
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", id));

        Restaurant restaurant = modelMapper.map(restaurantDTO, Restaurant.class);
        restaurant.setId(id);
        Restaurant saved = restaurantRepository.save(restaurant);
        return modelMapper.map(saved, RestaurantDTO.class);
    }

    public Set<MenuDTO> getMenuByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));
        Set<Menu> menus = restaurant.getMenus();
        return menus.stream().map((element) -> modelMapper.map(element, MenuDTO.class)).collect(Collectors.toSet());
    }

}
