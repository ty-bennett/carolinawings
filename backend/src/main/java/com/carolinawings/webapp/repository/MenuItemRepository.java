package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.domain.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
}
