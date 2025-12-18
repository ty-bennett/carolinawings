/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.config.ApplicationConstants;
import com.carolinawings.webapp.dto.*;
import com.carolinawings.webapp.service.MenuServiceImplementation;
import com.carolinawings.webapp.service.OrderServiceImplementation;
import com.carolinawings.webapp.service.RestaurantServiceImplementation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
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
    private final OrderServiceImplementation orderServiceImplementation;
    private final MenuServiceImplementation menuServiceImplementation;

    public RestaurantController(RestaurantServiceImplementation restaurantServiceImplementation, OrderServiceImplementation orderServiceImplementation, MenuServiceImplementation menuServiceImplementation) {
        this.restaurantServiceImplementation = restaurantServiceImplementation;
        this.orderServiceImplementation = orderServiceImplementation;
        this.menuServiceImplementation = menuServiceImplementation;

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

    //create order at restaurant

//    @PostMapping("/restaurants/{id}/orders")
//    @Transactional
//    public ResponseEntity<OrderDTO> createOrderByRestaurant(@Valid @RequestBody OrderDTO orderDTO, @PathVariable Long id)
//    {
//        OrderDTO savedOrderDTO = orderServiceImplementation.createOrderByRestaurant(id, orderDTO);
//        return new ResponseEntity<>(savedOrderDTO, HttpStatus.CREATED);
//    }

    // Get all orders by restaurant id with pagination
    @GetMapping("/restaurants/{id}/orders")
    public ResponseEntity<OrderResponse> getOrders(
            @RequestParam(name = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize,
            @PathVariable Long id)
    {
        return new ResponseEntity<>(orderServiceImplementation.getAllOrdersByRestaurantPaged(pageNumber, pageSize, id), HttpStatus.OK);
    }

    // Get all menus and paginate results
    @GetMapping("/restaurants/{restaurantId}/menus")
    public ResponseEntity<MenuResponse> getMenusByRestaurant(
            @RequestParam(name = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize,
            @PathVariable Long restaurantId) {
        return new ResponseEntity<>(menuServiceImplementation.getAllMenusByRestaurant(pageNumber, pageSize, restaurantId), HttpStatus.OK);
    }

    @GetMapping("/restaurants/{restaurantId}/menus/{menuId}")
    public ResponseEntity<MenuDTO> getMenuByRestaurant(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        return new ResponseEntity<>(menuServiceImplementation.getMenuByIdAndRestaurantId(restaurantId, menuId), HttpStatus.OK);
    }

    @PostMapping("/restaurants/{restaurantId}/menus")
    public ResponseEntity<MenuDTO> createMenuByRestaurant(@PathVariable Long restaurantId, @RequestBody MenuDTO menuDTO) {
        MenuDTO newMenu = menuServiceImplementation.createMenuByRestaurant(restaurantId, menuDTO);
        return new ResponseEntity<>(newMenu, HttpStatus.CREATED);
    }

    @PutMapping("/restaurants/{restaurantId}/menus/{menuId}")
    public ResponseEntity<MenuDTO> updateMenuByRestaurant(@PathVariable Long restaurantId, @PathVariable Long menuId, @RequestBody MenuDTO menuDTO) {
        MenuDTO menu = menuServiceImplementation.updateMenuByRestaurant(restaurantId, menuId, menuDTO);
        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @DeleteMapping("/restaurants/{restaurantId}/menus/{menuId}")
    public ResponseEntity<MenuDTO> deleteMenuByRestaurant(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        MenuDTO menu = menuServiceImplementation.deleteMenuByRestaurant(restaurantId, menuId);
        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @PutMapping("/restaurants/{restaurantId}/menus/{menuId}/primary")
    public ResponseEntity<MenuDTO> setPrimaryMenu(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        MenuDTO menu = menuServiceImplementation.setPrimaryMenu(restaurantId, menuId);
        return new ResponseEntity<>(menu, HttpStatus.OK);
    }
}
