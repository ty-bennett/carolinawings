/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.Menu;
import com.carolinawings.webapp.repository.MenuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImplementation implements MenuService {

    private MenuRepository menuRepository;

    public MenuServiceImplementation(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    @Override
    public Optional<Menu> getMenuById(Long id) {
        return menuRepository.findById(id);
    }

    @Override
    public String createMenu(Menu menu) {
        menuRepository.save(menu);
        return "Menu created with id "+menu.getId();
    }

    @Override
    public Menu updateMenu(Menu menu, Long id) {
        return menuRepository.findById(id)
                .map(existingMenu -> {
                    existingMenu.setName(menu.getName());
                    existingMenu.setMenuItemsList(existingMenu.getMenuItemsList());
                    return menuRepository.save(existingMenu);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found"));
    }

    @Override
    public String deleteMenuById(Long id) {
        if(!menuRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found");
        }
        menuRepository.deleteById(id);
        return "Menu deleted with id "+id;
    }
}
