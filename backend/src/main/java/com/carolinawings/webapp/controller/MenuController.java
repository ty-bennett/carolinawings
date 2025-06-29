/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.config.ApplicationConstants;
import com.carolinawings.webapp.dto.MenuDTO;
import com.carolinawings.webapp.dto.MenuItemDTO;
import com.carolinawings.webapp.dto.MenuResponse;
import com.carolinawings.webapp.model.MenuItem;
import com.carolinawings.webapp.service.MenuItemServiceImplementation;
import com.carolinawings.webapp.service.MenuServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class MenuController {
    private final MenuServiceImplementation menuServiceImplementation;
    private final MenuItemServiceImplementation menuItemServiceImplementation;

    public MenuController(MenuServiceImplementation menuServiceImplementation, MenuItemServiceImplementation menuItemServiceImplementation) {
        this.menuServiceImplementation = menuServiceImplementation;
        this.menuItemServiceImplementation = menuItemServiceImplementation;
    }

    // Get all menus
    @GetMapping("/menus/all")
    public ResponseEntity<MenuResponse> getMenus() {
        return new ResponseEntity<>(menuServiceImplementation.getAllMenus(), HttpStatus.OK);
    }

    // Get all menus and paginate results
    @GetMapping("/menus")
    public ResponseEntity<MenuResponse> getMenus(
            @RequestParam(name = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize) {
        return new ResponseEntity<>(menuServiceImplementation.getAllMenusPage(pageNumber, pageSize), HttpStatus.OK);
    }

    // Get a menu by its id and return it
    @GetMapping("/menus/{id}")
    public ResponseEntity<Optional<MenuDTO>> getMenuById(@PathVariable Long id) {
        return new ResponseEntity<>(menuServiceImplementation.getMenuById(id), HttpStatus.OK);
    }

    // Create a menu with valid object in request
    @PostMapping("/menus")
    public ResponseEntity<MenuDTO> createMenu(@Valid @RequestBody MenuDTO menuDTO) {
        MenuDTO savedMenuDTO = menuServiceImplementation.createMenu(menuDTO);
        return new ResponseEntity<>(savedMenuDTO, HttpStatus.CREATED);
    }

    // Delete a menu using its id
    @DeleteMapping("/menus/{id}")
    public ResponseEntity<MenuDTO> deleteMenuById(@PathVariable Long id) {
        MenuDTO deletedMenu = menuServiceImplementation.deleteMenu(id);
        return new ResponseEntity<>(deletedMenu, HttpStatus.OK);
    }

    // Change info in a menu
    @PutMapping("/menus/{id}")
    public ResponseEntity<MenuDTO> updateMenu(@Valid @RequestBody MenuDTO menuDTO,
                                              @PathVariable Long id) {
        MenuDTO savedMenuDTO = menuServiceImplementation.updateMenu(menuDTO, id);
        return new ResponseEntity<>(savedMenuDTO, HttpStatus.OK);
    }

    @PostMapping("/menus/{id}/menuitems")
    public ResponseEntity<MenuItemDTO> addProductToMenu(@PathVariable Long id, @Valid @RequestBody MenuItem menuItem)
    {
       MenuItemDTO responseMenuItem = menuItemServiceImplementation.addProductToMenu(id, menuItem);
       return new ResponseEntity<>(responseMenuItem, HttpStatus.CREATED);
    }
}
