package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.MenuItemOptionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemOptionRuleRepository extends JpaRepository<MenuItemOptionRule, Long> {
    Optional<MenuItemOptionRule> findByMenuItem_IdAndOptionTypeAndQuantity(Long menuItemId, String optionType, int quantity);
}
