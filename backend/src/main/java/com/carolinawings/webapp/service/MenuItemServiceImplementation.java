package com.carolinawings.webapp.service;

import com.carolinawings.webapp.repository.MenuItemRepository;
import com.carolinawings.webapp.model.MenuItem;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemServiceImplementation implements MenuItemService {

    private MenuItemRepository menuItemRepository;

    public MenuItemServiceImplementation(MenuItemRepository menuItemRepository) {
       this.menuItemRepository = menuItemRepository;
    }

    @Override
    public List<MenuItem> findAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @Override
    public Optional<MenuItem> findMenuItemById(Integer id) {
        return menuItemRepository.findById(id);
    }

    @Override
    public String createMenuItem(MenuItem menuItem) {
        menuItemRepository.save(menuItem);
        return "Menu item created with id: " + menuItem.getId();
    }

    @Override
    public String deleteMenuItem(Integer id) {
        if(!menuItemRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu item with id: " + id + " does not exist");
        menuItemRepository.deleteById(id);
        return "Menu item with id: " + id + " deleted";
    }

    public MenuItem updateMenuItem(MenuItem menuItem, Integer id) {
        return menuItemRepository.findById(id)
                .map(existingMenuItem -> {
                    existingMenuItem.setName(menuItem.getName());
                    existingMenuItem.setDescription(menuItem.getDescription());
                    existingMenuItem.setPrice(menuItem.getPrice());
                    return menuItemRepository.save(existingMenuItem);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu item with id: " + id + " does not exist"));
    }
}
