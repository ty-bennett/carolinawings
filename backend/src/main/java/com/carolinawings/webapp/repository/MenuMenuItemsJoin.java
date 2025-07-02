package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.MenuMenuItemJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuMenuItemsJoinRepository extends JpaRepository<MenuMenuItemsJoin, Long> {
        Optional<MenuMenuItemJoin> findMenuMenuItemsJoinBy(Long menuId, Integer menuItemId);
    }

}
