/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.config.ApplicationConstants;
import com.carolinawings.webapp.dto.*;
import com.carolinawings.webapp.model.MenuItem;
import com.carolinawings.webapp.service.MenuItemServiceImplementation;
import com.carolinawings.webapp.service.MenuServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PublicMenuController {
    private final MenuServiceImplementation menuServiceImplementation;
    private final MenuItemServiceImplementation menuItemServiceImplementation;

    public PublicMenuController(MenuServiceImplementation menuServiceImplementation, MenuItemServiceImplementation menuItemServiceImplementation) {
        this.menuServiceImplementation = menuServiceImplementation;
        this.menuItemServiceImplementation = menuItemServiceImplementation;
    }

    // Get a menu by its id and return it
    @GetMapping("/menus/{id}")
    public ResponseEntity<MenuDTO> getMenuById(@PathVariable Long id) {
        MenuDTO menu = menuServiceImplementation.getMenuById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found"));
        return ResponseEntity.ok(menu);
    }
//
//
//    @GetMapping("/menus/{id}/menuitems")
//    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByMenu(@PathVariable Long id,
//                                                                @RequestParam(name = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER) Integer pageNumber,
//                                                               @RequestParam(name = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE) Integer pageSize) {
//        return new ResponseEntity<>(menuItemServiceImplementation.getMenuItemsByMenu(id, pageNumber, pageSize), HttpStatus.OK);
//    }

}
