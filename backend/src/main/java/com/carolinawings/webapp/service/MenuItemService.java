/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.MenuItemDTO;
import com.carolinawings.webapp.dto.MenuItemResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public interface MenuItemService {
    MenuItemResponse getAllMenuItems();
    MenuItemResponse getAllMenuItemsPaged(Integer pageNumber, Integer pageSize, Long menuId);
    MenuItemResponse getAllMenuItemsSorted(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection, Long menuId);
    Optional<MenuItemDTO> getMenuItemById(Long id);
    MenuItemDTO createMenuItem(MenuItemDTO menuItem);
    MenuItemDTO deleteMenuItem(Long id);
    MenuItemDTO updateMenuItem(MenuItemDTO menuItem, Long id);

    MenuItemDTO addMenuItemToMenu(Long menuId, MenuItemDTO menuItem);

    List<MenuItemDTO> getMenuItemsByMenu(String menuId, Integer pageNumber, Integer pageSize);

    MenuItemDTO deleteMenuItemFromMenu(Long menuId, Long menuItemID);

    MenuItemDTO editMenuItemByMenu(@PathVariable Long menuId, @PathVariable Long menuItemId, @Valid @RequestBody MenuItemDTO menuItemDTO);

    //   MenuItemDTO addProductToMenu(Long menuId, MenuItem menuItem);
}
