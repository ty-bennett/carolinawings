package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.service.MenuItemService;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping
public class MenuItemController
{
    private final MenuItemService menuItemService;
    public MenuItemController(MenuItemService m)
    {
        this.menuItemService = m;
    }
}
