package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.model.MenuItem;
import com.carolinawings.webapp.service.MenuItemService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping
public class MenuItemController
{
    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService)
    {
        this.menuItemService = menuItemService;
    }

    @GetMapping("/api/v1/menu")
    public Iterable<MenuItem> getAllMenuItems()
    {
        return menuItemService.getAllById();
    }

    @GetMapping("/api/v1/menu/{id}")
    public Optional<MenuItem> getMenuItemById(@PathVariable Integer id)
    {
        return menuItemService.getById(id);
    }

    @PostMapping("/api/v1/auth/menu/add/item")
    public void addMenuItem(@RequestBody MenuItem menuItem)
    {
        System.out.println(menuItem);
        menuItemService.addMenuItem(menuItem);
    }

    @PutMapping("/api/v1/auth/menu/edit/item")
    public void updateMenuItem(@RequestBody MenuItem menuItem)
    {
        menuItemService.updateMenuItem(menuItem);
    }

    @DeleteMapping("api/v1/auth/menu/delete/{menuItem}")
    public void deleteMenuItem(@PathVariable MenuItem menuItem)
    {
        menuItemService.deleteMenuItem(menuItem.getId());
    }
}
