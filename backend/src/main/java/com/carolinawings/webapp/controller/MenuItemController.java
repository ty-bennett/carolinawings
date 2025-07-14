/*
Ty Bennett
*/

package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.config.ApplicationConstants;
import com.carolinawings.webapp.dto.MenuItemDTO;
import com.carolinawings.webapp.dto.MenuItemResponse;
import com.carolinawings.webapp.service.MenuItemServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carolinawings.webapp.model.MenuItem;

import java.util.List;
import java.util.Optional;

@RestController
public class MenuItemController {

    private final MenuItemServiceImplementation menuItemServiceImplementation;

    public MenuItemController(MenuItemServiceImplementation menuItemServiceImplementation) {
        this.menuItemServiceImplementation = menuItemServiceImplementation;
    }

    @GetMapping("/menuitems/all")
    public ResponseEntity<MenuItemResponse> getALlMenuItems() {
        return new ResponseEntity<>(menuItemServiceImplementation.getAllMenuItems(), HttpStatus.OK);
    }
    @GetMapping("/menus/{menuId}/menuitems")
    public ResponseEntity<MenuItemResponse> getAllMenuItemsPaged(@RequestParam(name="pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER) Integer pageNumber,
                                                            @RequestParam(name="pageSize", defaultValue = ApplicationConstants.PAGE_SIZE) Integer pageSize,
                                                            @PathVariable Long menuId){
        return new ResponseEntity<>(menuItemServiceImplementation.getAllMenuItemsPaged(pageNumber, pageSize, menuId), HttpStatus.OK);
    }

    @GetMapping("/menus/{menuId}/menuitems/sort")
    public ResponseEntity<MenuItemResponse> getAllMenuItemsSorted(@RequestParam(name="pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER) Integer pageNumber,
                                                            @RequestParam(name="pageSize", defaultValue = ApplicationConstants.PAGE_SIZE) Integer pageSize,
                                                            @RequestParam(name="sortBy", defaultValue = ApplicationConstants.SORT_MENU_ITEMS_BY, required = false) String sortBy,
                                                            @RequestParam(name="sortOrder", defaultValue = ApplicationConstants.SORT_MENU_ITEMS_ORDER, required = false) String sortOrder,
                                                            @PathVariable Long menuId){
        return new ResponseEntity<>(menuItemServiceImplementation.getAllMenuItemsSorted(pageNumber, pageSize, sortBy, sortOrder, menuId), HttpStatus.OK);
    }

    @GetMapping("/menuitems/{id}")
    public ResponseEntity<Optional<MenuItemDTO>> getMenuItemById(@PathVariable Long id) {
        return new ResponseEntity<>(menuItemServiceImplementation.getMenuItemById(id), HttpStatus.OK);
    }

    @PostMapping("/menuitems")
    public ResponseEntity<MenuItemDTO> createMenuItem(@Valid @RequestBody MenuItemDTO menuItemDTO) {
        MenuItemDTO savedMenuItemDTO = menuItemServiceImplementation.createMenuItem(menuItemDTO);
        return new ResponseEntity<>(savedMenuItemDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/menuitems/{id}")
    public ResponseEntity<MenuItemDTO> deleteMenuItemById(@PathVariable Long id) {
        return new ResponseEntity<>(menuItemServiceImplementation.deleteMenuItem(id), HttpStatus.OK);
    }

    @PutMapping("/menuitems/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(@Valid @RequestBody MenuItemDTO menuItemDTO,
                                                 @PathVariable Long id) {
        MenuItemDTO savedMenuItemDTO = menuItemServiceImplementation.updateMenuItem(menuItemDTO, id);
        return new ResponseEntity<>(savedMenuItemDTO, HttpStatus.OK);
    }

    @PostMapping("/menus/{id}/menuitems")
    public ResponseEntity<MenuItemDTO> addMenuItemToMenu(@PathVariable Long id, @Valid @RequestBody MenuItemDTO menuItem) {
        MenuItemDTO responseMenuItem = menuItemServiceImplementation.addMenuItemToMenu(id, menuItem);
        return new ResponseEntity<>(responseMenuItem, HttpStatus.CREATED);
    }
}