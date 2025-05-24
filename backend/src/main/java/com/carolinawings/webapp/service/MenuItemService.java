package com.carolinawings.webapp.service;

import com.carolinawings.webapp.repository.MenuItemRepository;
import com.carolinawings.webapp.domain.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


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
