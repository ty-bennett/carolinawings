package com.carolinawings.webapp.service;

import com.carolinawings.webapp.repository.MenuItemRepository;
import com.carolinawings.webapp.model.MenuItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository)
    {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> getAllMenuItems()
    {
        return menuItemRepository.findAll();
    }
    public Optional<MenuItem> getById(Integer id)
    {
        return menuItemRepository.findById(id);
    }
    public void addMenuItem (MenuItem menuItem)
    {
        menuItemRepository.save(menuItem);
    }

    public void deleteMenuItem(Integer id)
    {
        menuItemRepository.deleteById(id);
    }

    public void updateMenuItem(MenuItem menuItem) {
        menuItemRepository.findById(menuItem.getId());

    }
}
