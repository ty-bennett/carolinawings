/*
Written by Ty Bennett
 */

package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
//    Optional<MenuItem> findMenuItemById(Integer id);
//    List<MenuItem> findAll();
//    Optional<MenuItem> findById(Integer id);
//    void deleteById(Integer id);
//    Optional<MenuItem> createMenuItem(MenuItem menuItem);
//    Optional<MenuItem> updateMenuItem(MenuItem menuItem);

}
