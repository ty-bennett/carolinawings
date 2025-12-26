/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.config.ApplicationConstants;
import com.carolinawings.webapp.dto.*;
import com.carolinawings.webapp.service.MenuServiceImplementation;
import com.carolinawings.webapp.service.OrderServiceImplementation;
import com.carolinawings.webapp.service.RestaurantServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class RestaurantController {

    private final RestaurantServiceImplementation restaurantServiceImplementation;
    private final OrderServiceImplementation orderServiceImplementation;
    private final MenuServiceImplementation menuServiceImplementation;

    public RestaurantController(RestaurantServiceImplementation restaurantServiceImplementation, OrderServiceImplementation orderServiceImplementation, MenuServiceImplementation menuServiceImplementation) {
        this.restaurantServiceImplementation = restaurantServiceImplementation;
        this.orderServiceImplementation = orderServiceImplementation;
        this.menuServiceImplementation = menuServiceImplementation;

    }

    // Get all restaurants with pagination
    @GetMapping("/restaurants")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RESTAURANT_ADMIN', 'MANAGER')")
    public ResponseEntity<RestaurantResponse> getAllRestaurants(
            @RequestParam(name = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize) {
        return new ResponseEntity<>(restaurantServiceImplementation.getAllRestaurantsPaged(pageNumber, pageSize), HttpStatus.OK);
    }

    // Get a restaurant by its UUID
    @GetMapping("/restaurants/{restaurantId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RESTAURANT_ADMIN', 'MANAGER')")
    public ResponseEntity<Optional<RestaurantDTO>> getRestaurantById(@PathVariable Long restaurantId) {
        return new ResponseEntity<>(restaurantServiceImplementation.getRestaurantById(restaurantId), HttpStatus.OK);
    }

    // Create a new restaurant
    @PostMapping("/restaurants")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RestaurantDTO> createRestaurant(@Valid @RequestBody RestaurantDTO restaurantDTO) {
        RestaurantDTO savedRestaurantDTO = restaurantServiceImplementation.createRestaurant(restaurantDTO);
        return new ResponseEntity<>(savedRestaurantDTO, HttpStatus.CREATED);
    }

    // Delete a restaurant by UUID
    @DeleteMapping("/restaurants/{restaurantId}")
    @PreAuthorize("@securityService.canManageRestaurant(#restaurantId)")
    public ResponseEntity<RestaurantDTO> deleteRestaurant(@PathVariable Long restaurantId) {
        RestaurantDTO deletedRestaurant = restaurantServiceImplementation.deleteRestaurant(restaurantId);
        return new ResponseEntity<>(deletedRestaurant, HttpStatus.OK);
    }

    // Update restaurant info
    @PutMapping("/restaurants/{restaurantId}")
    @PreAuthorize("@securityService.canManageRestaurant(#restaurantId)")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@Valid @RequestBody RestaurantDTO restaurantDTO,
                                                          @PathVariable Long restaurantId) {
        RestaurantDTO updatedRestaurantDTO = restaurantServiceImplementation.updateRestaurant(restaurantDTO, restaurantId);
        return new ResponseEntity<>(updatedRestaurantDTO, HttpStatus.OK);
    }
    // Toggle accepting orders (busy mode)
    @PatchMapping("/restaurants/{restaurantId}/accepting-orders")
    @PreAuthorize("@securityService.canManageRestaurant(#restaurantId)")
    public ResponseEntity<RestaurantDTO> toggleAcceptingOrders(
            @PathVariable Long restaurantId,
            @RequestParam boolean accepting) {
        RestaurantDTO updated = restaurantServiceImplementation.setAcceptingOrders(restaurantId, accepting);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Update estimated pickup time
    @PatchMapping("/restaurants/{restaurantId}/pickup-time")
    @PreAuthorize("@securityService.canManageRestaurant(#restaurantId)")
    public ResponseEntity<RestaurantDTO> updatePickupTime(
            @PathVariable Long restaurantId,
            @RequestParam Integer minutes) {
        RestaurantDTO updated = restaurantServiceImplementation.setEstimatedPickupMinutes(restaurantId, minutes);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Get restaurant hours
    @GetMapping("/restaurants/{restaurantId}/hours")
    @PreAuthorize("@securityService.canManageRestaurant(#restaurantId)")
    public ResponseEntity<List<RestaurantHoursDTO>> getRestaurantHours(@PathVariable Long restaurantId) {
        List<RestaurantHoursDTO> hours = restaurantServiceImplementation.getRestaurantHours(restaurantId);
        return new ResponseEntity<>(hours, HttpStatus.OK);
    }

    // Update restaurant hours
    @PutMapping("/restaurants/{restaurantId}/hours")
    @PreAuthorize("@securityService.canManageRestaurant(#restaurantId)")
    public ResponseEntity<List<RestaurantHoursDTO>> updateRestaurantHours(
            @PathVariable Long restaurantId,
            @RequestBody List<RestaurantHoursDTO> hours) {
        List<RestaurantHoursDTO> updated = restaurantServiceImplementation.updateRestaurantHours(restaurantId, hours);
        return new ResponseEntity<>(updated, HttpStatus.OK);
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
    @GetMapping("/restaurants/{restaurantId}/orders")
    @PreAuthorize("@securityService.canManageRestaurant(#restaurantId)")
    public ResponseEntity<OrderResponseDTO> getOrders(
            @RequestParam(name = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize,
            @PathVariable Long restaurantId)
    {
        return new ResponseEntity<>(orderServiceImplementation.getAllOrdersByRestaurantPaged(pageNumber, pageSize, restaurantId), HttpStatus.OK);
    }

//    // Get all menus and paginate results
//    @GetMapping("/restaurants/{restaurantId}/menus")
//    public ResponseEntity<MenuResponse> getMenusByRestaurant(
//            @RequestParam(name = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
//            @RequestParam(name = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize,
//            @PathVariable Long restaurantId) {
//        return new ResponseEntity<>(menuServiceImplementation.getAllMenusByRestaurant(pageNumber, pageSize, restaurantId), HttpStatus.OK);
//    }

    @GetMapping("/restaurants/{restaurantId}/menus/{menuId}")
    @PreAuthorize("@securityService.canManageRestaurant(#restaurantId)")
    public ResponseEntity<MenuDTO> getMenuByRestaurant(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        return new ResponseEntity<>(menuServiceImplementation.getMenuByIdAndRestaurantId(restaurantId, menuId), HttpStatus.OK);
    }
//
//    @PostMapping("/restaurants/{restaurantId}/menus")
//    @PreAuthorize("@securityService.canManageRestaurant(#restaurantId)")
//    public ResponseEntity<MenuDTO> createMenuByRestaurant(@PathVariable Long restaurantId, @RequestBody MenuDTO menuDTO) {
//        MenuDTO newMenu = menuServiceImplementation.createMenuByRestaurant(restaurantId, menuDTO);
//        return new ResponseEntity<>(newMenu, HttpStatus.CREATED);
////    }
//
//    @PutMapping("/restaurants/{restaurantId}/menus/{menuId}")
//    @PreAuthorize("@securityService.canManageRestaurant(#restaurantId)")
//    public ResponseEntity<MenuDTO> updateMenuByRestaurant(@PathVariable Long restaurantId, @PathVariable Long menuId, @RequestBody MenuDTO menuDTO) {
//        MenuDTO menu = menuServiceImplementation.updateMenuByRestaurant(restaurantId, menuId, menuDTO);
//        return new ResponseEntity<>(menu, HttpStatus.OK);
//    }
//
    @PutMapping("/restaurants/{restaurantId}/menus/{menuId}/primary")
    @PreAuthorize("@securityService.canManageRestaurant(#restaurantId)")
    public ResponseEntity<MenuDTO> setPrimaryMenu(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        MenuDTO menu = menuServiceImplementation.setPrimaryMenu(restaurantId, menuId);
        return new ResponseEntity<>(menu, HttpStatus.OK);
    }
}
