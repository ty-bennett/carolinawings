package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.MenuItemOptionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemOptionRuleRepository extends JpaRepository<MenuItemOptionRule, Long> {
    List<MenuItemOptionRule> findByMenuItem_IdAndOptionType(Long menuItemId, String optionType);
}
