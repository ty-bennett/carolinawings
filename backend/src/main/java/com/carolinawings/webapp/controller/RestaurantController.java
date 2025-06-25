/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.config.ApplicationConstants;
import com.carolinawings.webapp.dto.RestaurantDTO;
import com.carolinawings.webapp.dto.RestaurantResponse;
import com.carolinawings.webapp.service.RestaurantServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class RestaurantController {

    private final RestaurantServiceImplementation restaurantServiceImplementation;

    public RestaurantController(RestaurantServiceImplementation restaurantServiceImplementation) {
        this.restaurantServiceImplementation = restaurantServiceImplementation;
    }

    // Get all restaurants
    @GetMapping("/restaurants/all")
    public ResponseEntity<RestaurantResponse> getRestaurants() {
        return new ResponseEntity<>(restaurantServiceImplementation.getAllRestaurants(), HttpStatus.OK);
    }

    // Get all restaurants with pagination
    @GetMapping("/restaurants")
    public ResponseEntity<RestaurantResponse> getRestaurants(
            @RequestParam(name = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize) {
        return new ResponseEntity<>(restaurantServiceImplementation.getAllRestaurantsPaged(pageNumber, pageSize), HttpStatus.OK);
    }

    // Get a restaurant by its UUID
    @GetMapping("/restaurants/{id}")
    public ResponseEntity<Optional<RestaurantDTO>> getRestaurantById(@PathVariable UUID id) {
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
    public ResponseEntity<RestaurantDTO> deleteRestaurant(@PathVariable UUID id) {
        RestaurantDTO deletedRestaurant = restaurantServiceImplementation.deleteRestaurant(id);
        return new ResponseEntity<>(deletedRestaurant, HttpStatus.OK);
    }

    // Update restaurant info
    @PutMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@Valid @RequestBody RestaurantDTO restaurantDTO,
                                                          @PathVariable UUID id) {
        RestaurantDTO updatedRestaurantDTO = restaurantServiceImplementation.updateRestaurant(restaurantDTO, id);
        return new ResponseEntity<>(updatedRestaurantDTO, HttpStatus.OK);
    }
}
