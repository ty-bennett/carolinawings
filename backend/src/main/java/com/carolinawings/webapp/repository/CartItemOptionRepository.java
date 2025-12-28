package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.CartItem;
import com.carolinawings.webapp.model.CartItemOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemOptionRepository extends JpaRepository<CartItemOption, Long> {
    List<CartItemOption> findByCartItem(CartItem cartItem);
}
