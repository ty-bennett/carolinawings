/*
Written by Ty Bennett
 */

package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.MenuItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends CrudRepository<MenuItem, Integer> {
    Optional<MenuItem> findById(Integer id);
    Optional<MenuItem> findByName(String name);
    Optional<MenuItem> findByPrice(BigDecimal price);
}
