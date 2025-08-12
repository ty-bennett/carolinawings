package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.MenuItemOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuItemOptionRepository extends JpaRepository<MenuItemOption, Long> {
    Optional<MenuItemOption> findByName(String optionName);
}
