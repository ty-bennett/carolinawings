package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.service.MenuItemService;
import com.carolinawings.webapp.service.MenuItemServiceImplementation;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carolinawings.webapp.model.MenuItem;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return new ResponseEntity<>(menuItemServiceImplementation.findAllMenuItems(), HttpStatus.OK);
    }

    @GetMapping("/menuitems/{id}")
    public ResponseEntity<Optional<MenuItem>> getMenuItemById(Integer id) {
        return new ResponseEntity<>(menuItemServiceImplementation.findMenuItemById(id), HttpStatus.OK);
    }

    @PostMapping("/menuitems")
    public ResponseEntity<String> createMenuItem(@RequestBody MenuItem m) {
        menuItemServiceImplementation.createMenuItem(m);
        return new ResponseEntity<>("Menu item created successfully " + m, HttpStatus.CREATED);
    }

    @DeleteMapping("/menuitems/{id}")
    public ResponseEntity<String> deleteMenuItemById(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(menuItemServiceImplementation.deleteMenuItem(id), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());

        }
    }
    @PutMapping("/menuitems/{id}")
    public ResponseEntity<String> updateMenuItem(@RequestBody MenuItem menuItem,
                                                 @PathVariable Integer id)
    {
       try {
           menuItemServiceImplementation.updateMenuItem(menuItem, id);
           return new ResponseEntity<>("company edited with existing id" + id, HttpStatus.OK);
       } catch (ResponseStatusException e) {
           return new ResponseEntity<>(e.getReason(), e.getStatusCode());
       }
    }

}
