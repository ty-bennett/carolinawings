package com.carolinawings.webapp.controller;


import com.carolinawings.webapp.model.Permission;
import com.carolinawings.webapp.model.Restaurant;
import com.carolinawings.webapp.service.RestaurantServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class RestaurantController {

    private final RestaurantServiceImplementation restaurantServiceImplementation;

    public RestaurantController(RestaurantServiceImplementation restaurantServiceImplementation)
    {
        this.restaurantServiceImplementation = restaurantServiceImplementation;
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<Restaurant>> getRestaurants()
    {
        return new ResponseEntity<>(restaurantServiceImplementation.getAllRestaurants(), HttpStatus.OK);
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<Optional<Restaurant>> getRestaurantById(@PathVariable UUID id)
    {
        return new ResponseEntity<>(restaurantServiceImplementation.getRestaurantById(id), HttpStatus.OK);
    }

    @PostMapping("/restaurants")
    public ResponseEntity<String> createRestaurant(@RequestBody Restaurant r)
    {
        restaurantServiceImplementation.createRestaurant(r);
        return new ResponseEntity<>("Restaurant created successfully \n" + r, HttpStatus.CREATED);
    }

    @DeleteMapping("/restaurants/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable UUID id)
    {
        try {
            return new ResponseEntity<>(restaurantServiceImplementation.deleteRestaurant(id), HttpStatus.OK);
        } catch (ResponseStatusException e)
        {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/restaurants/{id}")
    public ResponseEntity<String> updateRestaurant(@RequestBody Restaurant r,
                                                    @PathVariable UUID id)
    {
        try {
            restaurantServiceImplementation.updateRestaurant(r, id);
            return new ResponseEntity<>("Edited manager with id: "+id+" \n" +r, HttpStatus.OK);
        } catch(ResponseStatusException e)
        {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
