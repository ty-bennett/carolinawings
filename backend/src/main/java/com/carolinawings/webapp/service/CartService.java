package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.AddCartItemDTO;
import com.carolinawings.webapp.dto.CartDTO;
import com.carolinawings.webapp.model.MenuItemOption;
import com.carolinawings.webapp.model.MenuItemOptionGroup;

import java.util.List;
import java.util.Map;

public interface CartService {
    CartDTO addMenuItemToCart(AddCartItemDTO addCartItemDTO);
    CartDTO removeMenuItemFromCart(Long menuItemId);
    CartDTO updateCartItemQuantity(Long menuItemId, Integer quantity);
    CartDTO clearCart();
}
