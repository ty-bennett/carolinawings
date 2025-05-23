package com.carolinawings.webapp;

import com.carolinawings.webapp.domain.MenuItem;
import com.carolinawings.webapp.repository.MenuItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu")
public class MenuItemController {

    private final MenuItemRepository menuRepository;

    public MenuItemController(MenuItemRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @GetMapping
    public List<MenuItem> findAllMenuItems() {
        return menuRepository.findAll();
    }
}
