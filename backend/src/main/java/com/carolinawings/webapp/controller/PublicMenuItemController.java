package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.dto.MenuItemDTO;
import com.carolinawings.webapp.dto.MenuItemResponse;
import com.carolinawings.webapp.service.MenuItemServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class PublicMenuItemController {

    private final MenuItemServiceImplementation menuItemService;

    public PublicMenuItemController(MenuItemServiceImplementation menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping("/menus/{menuId}/items")
    public ResponseEntity<MenuItemResponse> getMenuItemsByMenu(
            @PathVariable Long menuId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<>(menuItemService.getAllMenuItemsPaged(page, size, menuId), HttpStatus.OK);
    }

    @GetMapping("/menuitems/{menuItemId}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Long menuItemId) {
        return new ResponseEntity<>(menuItemService.getMenuItemById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found")), HttpStatus.OK);
    }
}