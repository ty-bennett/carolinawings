package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.dto.RestaurantDTO;
import com.carolinawings.webapp.dto.RestaurantResponse;
import com.carolinawings.webapp.service.RestaurantServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class PublicRestaurantController {

    private final RestaurantServiceImplementation restaurantService;

    public PublicRestaurantController(RestaurantServiceImplementation restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurants")
    public ResponseEntity<RestaurantResponse> getAllRestaurants() {
        return new ResponseEntity<>(restaurantService.getAllRestaurants(), HttpStatus.OK);
    }

    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Long restaurantId) {
        return  new ResponseEntity<>(restaurantService.getRestaurantById(restaurantId), HttpStatus.OK);
    }
}