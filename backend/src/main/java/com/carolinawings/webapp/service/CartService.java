package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.AddCartItemDTO;
import com.carolinawings.webapp.dto.CartDTO;

public interface CartService {
    public CartDTO addMenuItemToCart(AddCartItemDTO addCartItemDTO);
}
