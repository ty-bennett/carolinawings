package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.Cart;
import com.carolinawings.webapp.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.menuItem.id = ?2")
    CartItem findCartItemByMenuItemIdAndCartId(Long id, Long menuItemId);
    List<CartItem> findByCart(Cart cart);

    List<CartItem> findByMenuItemId(Long id);
    ;
}
