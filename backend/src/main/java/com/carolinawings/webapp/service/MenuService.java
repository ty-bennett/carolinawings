/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.Menu;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface MenuService {
    List<Menu> getAllMenus();
    Optional<Menu> getMenuById(Long id);
    String createMenu(Menu menu);
    Menu updateMenu(Menu menu, Long id);
    String deleteMenuById(Long id);
}
