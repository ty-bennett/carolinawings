package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.domain.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface   MenuItemRepository extends JpaRepository<MenuItem, Integer> {

}
