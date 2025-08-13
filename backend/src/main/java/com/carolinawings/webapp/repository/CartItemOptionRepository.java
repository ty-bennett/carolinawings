package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.Cart;
import com.carolinawings.webapp.model.CartItem;
import com.carolinawings.webapp.model.CartItemChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemOptionRepository extends JpaRepository<CartItemChoice, Long> {
    List<CartItemChoice> findByCartItem(CartItem cartItem);
}
