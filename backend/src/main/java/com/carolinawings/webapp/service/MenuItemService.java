package com.carolinawings.webapp.service;

import com.carolinawings.webapp.repository.MenuItemRepository;
import com.carolinawings.webapp.model.MenuItem;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public Optional<MenuItem> findByName(String name) {
        return menuItemRepository.findByName(name);
    }
}
