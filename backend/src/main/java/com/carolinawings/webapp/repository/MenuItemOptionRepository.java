package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.MenuItem;
import com.carolinawings.webapp.model.MenuItemOption;
import com.carolinawings.webapp.model.MenuItemOptionGroup;
import com.carolinawings.webapp.model.OptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemOptionRepository extends JpaRepository<MenuItemOption, Long> {
    Optional<MenuItemOption> findByOptionGroupIdAndId(Long optionGroupId, Long id);
    Optional<MenuItemOption> findByOptionGroupIdAndNameIgnoreCase(Long optionGroupId, String name);
    Optional<MenuItemOption> findByOptionGroup(OptionGroup optionGroup);
}
