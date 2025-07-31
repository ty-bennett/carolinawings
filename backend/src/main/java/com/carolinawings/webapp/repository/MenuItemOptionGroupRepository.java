package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.MenuItemOptionGroup;
import com.carolinawings.webapp.model.OptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuItemOptionGroupRepository extends JpaRepository<MenuItemOptionGroup, Long> {
    Optional<MenuItemOptionGroup> findByMenuItem_Id(Long menuItemId);
}
