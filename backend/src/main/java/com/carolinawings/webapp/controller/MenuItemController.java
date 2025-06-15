package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.service.MenuItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carolinawings.webapp.model.MenuItem;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping
public class MenuItemController
{
    private final MenuItemService menuItemService;
    public MenuItemController(MenuItemService m)
    {
        this.menuItemService = m;
    }
    @PostMapping("/auth/admin/menuitems")
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItem m)
    {
        return ResponseEntity.ok(m);
    }

    @GetMapping("/menuitems")
    public List<MenuItem> getAllMenuItems()
    {
        return menuItemService.getAllById();
    }
    @GetMapping("menuitems/{id}")
    public Optional<MenuItem> getMenuItemById(@PathVariable Integer id)
    {
        return menuItemService.getById(id);
    }
}
