/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.MenuDTO;
import com.carolinawings.webapp.dto.MenuResponse;

import java.util.Optional;

public interface MenuService {
    MenuResponse getAllMenusByRestaurant(Integer page, Integer pageSize, Long restaurantId);
    Optional<MenuDTO> getMenuById(Long id);
    MenuDTO createMenu(MenuDTO menu);
    MenuDTO deleteMenu(Long id);
    MenuDTO updateMenu(MenuDTO menu, Long id);

    MenuDTO getMenuByIdAndRestaurantId(Long restaurantId, Long menuId);
}
