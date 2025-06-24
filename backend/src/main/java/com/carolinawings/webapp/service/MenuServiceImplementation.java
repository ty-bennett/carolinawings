/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Menu;
import com.carolinawings.webapp.repository.MenuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImplementation implements MenuService {

    private final MenuRepository menuRepository;

    public MenuServiceImplementation(MenuRepository menuRepository)
    {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> getAllMenus()
    {
        List<Menu> menus = menuRepository.findAll();
        if(menus.isEmpty())
            throw new APIException("No menus present");
        return menus;
    }

    @Override
    public String createMenu(Menu menu) {
        Menu savedMenu = menuRepository.findByName(menu.getName());
        if(savedMenu != null)
            throw new APIException("Menu with the name " + menu.getName() + " already exists");
        menuRepository.save(menu);
        return "Menu with id " + menu.getId() + " added successfully";
    }

    @Override
    public Optional<Menu> getMenuById(Long id) {
        return menuRepository.findById(id);
    }

    @Override
    public String deleteMenuById(Long id) {
        Menu m = menuRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu", "menuId", id));
        menuRepository.delete(m);
        return "Menu with id " + id + " deleted successfully";
    }

    @Override
    public Menu updateMenu(Menu menu, Long id) {
        Menu savedMenu = menuRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu", "menuId", id));
        menu.setId(id);
        return menuRepository.save(savedMenu);
    }
}
