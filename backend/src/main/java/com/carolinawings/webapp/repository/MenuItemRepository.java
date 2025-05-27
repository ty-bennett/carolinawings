/*
Written by Ty Bennett
 */

package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.MenuItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuItemRepository extends CrudRepository<MenuItem, Long> {
    Optional<MenuItem> findByName(String name);
}
