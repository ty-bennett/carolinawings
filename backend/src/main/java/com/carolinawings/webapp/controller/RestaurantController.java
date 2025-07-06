/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.config.ApplicationConstants;
import com.carolinawings.webapp.dto.MenuDTO;
import com.carolinawings.webapp.dto.OrderDTO;
import com.carolinawings.webapp.dto.RestaurantDTO;
import com.carolinawings.webapp.dto.RestaurantResponse;
import com.carolinawings.webapp.service.OrderServiceImplementation;
import com.carolinawings.webapp.service.RestaurantServiceImplementation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class RestaurantController {

    private final RestaurantServiceImplementation restaurantServiceImplementation;
    private OrderServiceImplementation orderServiceImplementation;

    public RestaurantController(RestaurantServiceImplementation restaurantServiceImplementation, OrderServiceImplementation orderServiceImplementation) {
        this.restaurantServiceImplementation = restaurantServiceImplementation;
        this.orderServiceImplementation = orderServiceImplementation;
    }

    // Get all restaurants
    @GetMapping("/restaurants/all")
    public ResponseEntity<RestaurantResponse> getRestaurants() {
        return new ResponseEntity<>(restaurantServiceImplementation.getAllRestaurants(), HttpStatus.OK);
    }

    // Get all restaurants with pagination
    @GetMapping("/restaurants")
    public ResponseEntity<RestaurantResponse> getAllRestaurants(
            @RequestParam(name = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize) {
        return new ResponseEntity<>(restaurantServiceImplementation.getAllRestaurantsPaged(pageNumber, pageSize), HttpStatus.OK);
    }

    // Get a restaurant by its UUID
    @GetMapping("/restaurants/{id}")
    public ResponseEntity<Optional<RestaurantDTO>> getRestaurantById(@PathVariable Long id) {
        return new ResponseEntity<>(restaurantServiceImplementation.getRestaurantById(id), HttpStatus.OK);
    }

    // Create a new restaurant
    @PostMapping("/restaurants")
    public ResponseEntity<RestaurantDTO> createRestaurant(@Valid @RequestBody RestaurantDTO restaurantDTO) {
        RestaurantDTO savedRestaurantDTO = restaurantServiceImplementation.createRestaurant(restaurantDTO);
        return new ResponseEntity<>(savedRestaurantDTO, HttpStatus.CREATED);
    }

    // Delete a restaurant by UUID
    @DeleteMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantDTO> deleteRestaurant(@PathVariable Long id) {
        RestaurantDTO deletedRestaurant = restaurantServiceImplementation.deleteRestaurant(id);
        return new ResponseEntity<>(deletedRestaurant, HttpStatus.OK);
    }

    // Update restaurant info
    @PutMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@Valid @RequestBody RestaurantDTO restaurantDTO,
                                                          @PathVariable Long id) {
        RestaurantDTO updatedRestaurantDTO = restaurantServiceImplementation.updateRestaurant(restaurantDTO, id);
        return new ResponseEntity<>(updatedRestaurantDTO, HttpStatus.OK);
    }

    @GetMapping("/restaurants/{restaurantId}/menus")
    public ResponseEntity<Set<MenuDTO>> getMenusByRestaurant(@PathVariable Long restaurantId)
    {
        Set<MenuDTO> menuDTOS = restaurantServiceImplementation.getMenuByRestaurant(restaurantId);
        return new ResponseEntity<>(menuDTOS, HttpStatus.OK);
    }
    //create order at restaurant

    @PostMapping("/restaurants/{id}/orders/create")
    @Transactional
    public ResponseEntity<OrderDTO> createOrderByRestaurant(@Valid @RequestBody OrderDTO orderDTO, @PathVariable Long id)
    {
        OrderDTO savedOrderDTO = orderServiceImplementation.createOrderByRestaurant(id, orderDTO);
        return new ResponseEntity<>(savedOrderDTO, HttpStatus.CREATED);
    }

}
