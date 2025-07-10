package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.Menu;
import com.carolinawings.webapp.model.MenuMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuMenuItemRepository extends JpaRepository<MenuMenuItem, Long> {
    // Find by composite keys (Menu ID + MenuItem ID)
    Optional<MenuMenuItem> findByMenuIdAndMenuItemId(Long menuId, Integer menuItemId);

    // Optionally, list all menu items for a given menu
    List<MenuMenuItem> findAllByMenuId(Long menuId);

    // List all menus containing a specific menu item
    List<MenuMenuItem> findByMenuItemId(Integer menuItemId);

    Long menu(Menu menu);
}
