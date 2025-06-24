/*
Ty Bennett
*/

package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.service.MenuItemServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carolinawings.webapp.model.MenuItem;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class MenuItemController {

    private final MenuItemServiceImplementation menuItemServiceImplementation;

    public MenuItemController(MenuItemServiceImplementation menuItemServiceImplementation) {
        this.menuItemServiceImplementation = menuItemServiceImplementation;
    }

    @GetMapping("/menuitems")
    public ResponseEntity<List<MenuItem>> getMenuItems() {
        return new ResponseEntity<>(menuItemServiceImplementation.findAllMenuItems(), HttpStatus.OK);
    }

    @GetMapping("/menuitems/{id}")
    public ResponseEntity<Optional<MenuItem>> getMenuItemById(@PathVariable Integer id) {
        return new ResponseEntity<>(menuItemServiceImplementation.findMenuItemById(id), HttpStatus.OK);
    }

    @PostMapping("/menuitems")
    public ResponseEntity<String> createMenuItem(@Valid @RequestBody MenuItem menuItem) {
        menuItemServiceImplementation.createMenuItem(menuItem);
        return new ResponseEntity<>("Menu item created successfully:\n" + menuItem, HttpStatus.CREATED);
    }

    @DeleteMapping("/menuitems/{id}")
    public ResponseEntity<String> deleteMenuItemById(@PathVariable Integer id) {
        return new ResponseEntity<>(menuItemServiceImplementation.deleteMenuItem(id), HttpStatus.OK);
    }

    @PutMapping("/menuitems/{id}")
    public ResponseEntity<String> updateMenuItem(@Valid @RequestBody MenuItem menuItem,
                                                 @PathVariable Integer id) {
        menuItemServiceImplementation.updateMenuItem(menuItem, id);
        return new ResponseEntity<>("Menu item edited with existing id: " + id, HttpStatus.OK);
    }
}