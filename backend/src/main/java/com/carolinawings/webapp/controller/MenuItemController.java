/*
Ty Bennett
*/

package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.config.ApplicationConstants;
import com.carolinawings.webapp.dto.MenuItemDTO;
import com.carolinawings.webapp.dto.MenuItemResponse;
import com.carolinawings.webapp.model.Menu;
import com.carolinawings.webapp.repository.MenuItemRepository;
import com.carolinawings.webapp.repository.MenuRepository;
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
    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;

    public MenuItemController(MenuItemServiceImplementation menuItemServiceImplementation, MenuRepository menuRepository, MenuItemRepository menuItemRepository) {
        this.menuItemServiceImplementation = menuItemServiceImplementation;
        this.menuRepository = menuRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @GetMapping("/menuitems/all")
    public ResponseEntity<MenuItemResponse> getALlMenuItems() {
        return new ResponseEntity<>(menuItemServiceImplementation.getAllMenuItems(), HttpStatus.OK);
    }

    @GetMapping("/menus/{menuId}/menuitems/paged")
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

    @PutMapping("/menus/{id}/menuitems/{menuItemId}")
    public ResponseEntity<MenuItemDTO> editMenuItemByMenu(@PathVariable Long id,@PathVariable Long menuItemId, @Valid @RequestBody MenuItemDTO menuItemDTO) {
        MenuItemDTO menuItem = menuItemServiceImplementation.editMenuItemByMenu(id, menuItemId, menuItemDTO);
        return new ResponseEntity<>(menuItem, HttpStatus.OK);
    }

    @DeleteMapping("/menus/{id}/menuitems/{menuitemid}")
    public ResponseEntity<MenuItemDTO> deleteMenuItemFromMenu(@PathVariable Long id, @PathVariable Long menuitemid)
    {
        MenuItemDTO responseMenuItem = menuItemServiceImplementation.deleteMenuItemFromMenu(id, menuitemid);
        return new ResponseEntity<>(responseMenuItem, HttpStatus.OK);
    }
}