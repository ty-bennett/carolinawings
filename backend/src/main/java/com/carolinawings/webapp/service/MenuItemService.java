/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.MenuItemDTO;
import com.carolinawings.webapp.dto.MenuItemResponse;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public interface MenuItemService {
    MenuItemResponse getAllMenuItems();
    MenuItemResponse getAllMenuItemsPaged(Integer pageNumber, Integer pageSize);
    MenuItemResponse getAllMenuItemsSorted(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection);
    Optional<MenuItemDTO> getMenuItemById(Integer id);
    MenuItemDTO createMenuItem(MenuItemDTO menuItem);
    MenuItemDTO deleteMenuItem(Integer id);
    MenuItemDTO updateMenuItem(MenuItemDTO menuItem, Integer id);
}
