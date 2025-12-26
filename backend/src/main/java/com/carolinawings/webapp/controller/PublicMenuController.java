package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.dto.MenuDTO;
import com.carolinawings.webapp.dto.MenuResponse;
import com.carolinawings.webapp.service.MenuServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class PublicMenuController {

    private final MenuServiceImplementation menuService;

    public PublicMenuController(MenuServiceImplementation menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/restaurants/{restaurantId}/menus")
    public ResponseEntity<MenuResponse> getMenusByRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<>(menuService.getAllMenusByRestaurant(page, size, restaurantId), HttpStatus.OK);
    }

    @GetMapping("/restaurants/{restaurantId}/menus/{menuId}")
    public ResponseEntity<MenuDTO> getMenuById(
            @PathVariable Long restaurantId,
            @PathVariable Long menuId) {
        return new ResponseEntity<>(menuService.getMenuByIdAndRestaurantId(restaurantId, menuId), HttpStatus.OK);
    }
}