package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.CartItemChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemOptionRepository extends JpaRepository<CartItemChoice, Long> {
}
