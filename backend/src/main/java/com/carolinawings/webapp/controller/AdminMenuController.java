package com.carolinawings.webapp.controller;


import com.carolinawings.webapp.dto.MenuDTO;
import com.carolinawings.webapp.service.MenuServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class AdminMenuController {

    private final MenuServiceImplementation menuServiceImplementation;

    public AdminMenuController(MenuServiceImplementation menuServiceImplementation) {
        this.menuServiceImplementation = menuServiceImplementation;
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

}
