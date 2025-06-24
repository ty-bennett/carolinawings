package com.carolinawings.webapp.service;

import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.repository.MenuItemRepository;
import com.carolinawings.webapp.model.MenuItem;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemServiceImplementation implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemServiceImplementation(MenuItemRepository menuItemRepository) {
       this.menuItemRepository = menuItemRepository;
    }
    public List<MenuItem> findAllMenuItems ()
    {
        List<MenuItem> items = menuItemRepository.findAll();
        if(items.isEmpty())
            throw new APIException("No Menu Items present");
        return items;
    }

    @Override
    public String createMenuItem(MenuItem menuItem) {
        MenuItem savedMenuItem = menuItemRepository.findByName(menuItem.getName());
        if(savedMenuItem != null)
            throw new APIException("Company with the name "+ menuItem.getName() + " already exists");
        menuItemRepository.save(menuItem);
        return "Company with id " +menuItem.getId()+" added successfully";
    }

    @Override
    public Optional<MenuItem> findMenuItemById (Integer id) {
        return menuItemRepository.findById(id);
    }

    @Override
    public String deleteMenuItem(Integer id) {
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Company", "companyId", Long.valueOf(id)));
        menuItemRepository.delete(menuItem);
        return "Menu Item with id " + id + " deleted successfully";
    }


    @Override
    public MenuItem updateMenuItem(MenuItem menuItem, Integer id) {
        MenuItem savedMenuItem = menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Company", "companyId: ", Long.valueOf(id)));
        menuItem.setId(id);
        return menuItemRepository.save(savedMenuItem);
    }
}
