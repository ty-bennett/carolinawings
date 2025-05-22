package com.carolinawings.webapp.service;

import com.carolinawings.webapp.repository.MenuItemRepository;
import com.carolinawings.webapp.domain.MenuItem;

import java.util.List;
import java.util.UUID;



public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> findAll(UUID id) {
        return menuItemRepository.findAll();
    }
}
