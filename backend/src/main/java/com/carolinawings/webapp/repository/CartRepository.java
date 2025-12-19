package com.carolinawings.webapp.repository;


import com.carolinawings.webapp.model.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.username = ?1")
    Cart findCartByUserEmail(String userEmail);
    @Query("SELECT c FROM Cart c " +
            "LEFT JOIN FETCH c.cartItems ci " +
            "LEFT JOIN FETCH ci.choices ch " +
            "LEFT JOIN FETCH ch.menuItemOption o " +
            "WHERE c.id = ?1")
    Cart findCartByIdWithOptions(Long id);

    @Query("SELECT c FROM Cart c WHERE c.user.username = ?1 " +
            "AND c.id = ?2")
    Cart findCartByUserEmailAndId(String userEmail, Long id);

    Optional<Cart> findCartById(Long id);
}
