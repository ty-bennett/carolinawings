package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.model.MenuItem;
import com.carolinawings.webapp.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuItemController {

    MenuItemService menuItemService;
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }
    @RequestMapping("/api/v1/menu")
    public Iterable<MenuItem> findAllMenuItems() {
        return menuItemService.getAllById();
    }
}
