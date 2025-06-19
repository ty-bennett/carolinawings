/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.MenuItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface MenuItemService {
    List<MenuItem> findAllMenuItems();
    Optional<MenuItem> findMenuItemById(Integer id);
    String createMenuItem(MenuItem menuItem);
    String deleteMenuItem(Integer id);
    MenuItem updateMenuItem(MenuItem menuItem, Integer id);
}
