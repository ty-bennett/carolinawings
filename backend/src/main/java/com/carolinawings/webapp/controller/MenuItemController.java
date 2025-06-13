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

}
