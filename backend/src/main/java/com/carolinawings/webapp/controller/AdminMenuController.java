package com.carolinawings.webapp.controller;


import com.carolinawings.webapp.dto.MenuDTO;
import com.carolinawings.webapp.dto.MenuResponse;
import com.carolinawings.webapp.security.SecurityService;
import com.carolinawings.webapp.service.MenuServiceImplementation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminMenuController {

    private final MenuServiceImplementation menuServiceImplementation;

    // ==============================
    // GET all menus for a restaurant
    // ==============================
    @GetMapping("/restaurants/{restaurantId}/menus")
    @PreAuthorize("@securityService.canManageRestaurant(#restaurantId)")
    public ResponseEntity<MenuResponse> getMenusForRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        MenuResponse menuResponse =
                menuServiceImplementation.getAllMenusByRestaurant(page, size,  restaurantId);
        return ResponseEntity.ok(menuResponse);
    }

    // ==============================
    // POST create a menu for a restaurant
    // ==============================
    @PostMapping("/restaurants/{restaurantId}/menus")
    @PreAuthorize("@securityService.canManageRestaurant(#restaurantId)")
    public ResponseEntity<MenuDTO> createMenu(
            @PathVariable Long restaurantId,
            @Valid @RequestBody MenuDTO menuDTO) {

        MenuDTO createdMenu =
                menuServiceImplementation.createMenuByRestaurant(restaurantId, menuDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenu);
    }

    // ==============================
    // DELETE a menu for a restaurant
    // ==============================
    @DeleteMapping("/restaurants/{restaurantId}/menus/{menuId}")
    @PreAuthorize("@securityService.canManageRestaurant(#restaurantId)")
    public ResponseEntity<Void> deleteMenuByRestaurant(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        menuServiceImplementation.deleteMenuByRestaurant(restaurantId, menuId);
        return ResponseEntity.noContent().build();
    }

    // ==============================
    // UPDATE a menu for a restaurant
    // ==============================
    @PutMapping("/restaurants/{restaurantId}/menus/{menuId}")
    @PreAuthorize("@securityService.canManageRestaurant(#restaurantId)")
    public ResponseEntity<MenuDTO> updateMenu(
            @Valid @RequestBody MenuDTO menuDTO,
            @PathVariable Long restaurantId,
            @PathVariable Long menuId) {
        MenuDTO savedMenuDTO = menuServiceImplementation.updateMenu(menuDTO, restaurantId, menuId);
        return new ResponseEntity<>(savedMenuDTO, HttpStatus.OK);
    }

    // ===============================================
    // PATCH primary status on a menu for a restaurant
    // ===============================================
    @PatchMapping("/restaurants/{restaurantId}/menus/{menuId}/primary")
    @PreAuthorize("@securityService.canManageRestaurant(#restaurantId)")
    public ResponseEntity<MenuDTO> updatePrimaryMenu(
            @PathVariable Long restaurantId,
            @PathVariable Long menuId
    ) {
        MenuDTO menu =  menuServiceImplementation.setPrimaryMenu(restaurantId, menuId);
        return ResponseEntity.ok(menu);
    }
}
