package com.carolinawings.webapp.repository;


import com.carolinawings.webapp.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.username = ?1")
    Cart findCartByUserEmail(String userEmail);
}
