package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.model.Menu;
import com.carolinawings.webapp.service.MenuServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class MenuController {
    private final MenuServiceImplementation menuServiceImplementation;

    public MenuController(MenuServiceImplementation menuServiceImplementation) {
        this.menuServiceImplementation = menuServiceImplementation;
    }

    @GetMapping("/menus")
    public ResponseEntity<List<Menu>> getAllMenus () {

        return new ResponseEntity<>(menuServiceImplementation.getAllMenus(), HttpStatus.OK);
    }

    //gets a company by its id and returns a list to the user
    @GetMapping("/menus/{id}")
    public ResponseEntity<Optional<Menu>> getMenuById(@PathVariable Long id)
    {
        return new ResponseEntity<>(menuServiceImplementation.getMenuById(id), HttpStatus.OK);
    }

    @PostMapping("/menus")
    public ResponseEntity<String> createMenu(Menu menu)
    {
        menuServiceImplementation.createMenu(menu);
        return new ResponseEntity<>("Company created successfully: \n" + menu, HttpStatus.CREATED);
    }
    @DeleteMapping("/menus/{id}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long id)
    {
        try {
            return new ResponseEntity<>(menuServiceImplementation.deleteMenuById(id), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/menus/{id}")
    public ResponseEntity<String> updateMenu(@RequestBody Menu menu,
                                                @PathVariable Long id)
    {
        try {
            menuServiceImplementation.updateMenu(menu, id);
            return new ResponseEntity<>("Company edited with existing id:" + id, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
