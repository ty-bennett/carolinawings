package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.dto.AddCartItemDTO;
import com.carolinawings.webapp.dto.CartDTO;
import com.carolinawings.webapp.model.Cart;
import com.carolinawings.webapp.repository.CartRepository;
import com.carolinawings.webapp.service.CartService;
import com.carolinawings.webapp.service.CartServiceImplementation;
import com.carolinawings.webapp.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartServiceImplementation cartServiceImplementation;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/cart/items")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> addProductToCart(@RequestBody AddCartItemDTO addCartItemDTO)
    {
        CartDTO cartDTO = cartService.addMenuItemToCart(addCartItemDTO);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> getCartByUser() {
        CartDTO cartDTO = cartServiceImplementation.getUserCart(authUtil.loggedInEmail(), null);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/cart/items/{cartItemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> removeItemFromCart(@PathVariable Long cartItemId) {
        CartDTO cartDTO = cartService.removeCartItem(cartItemId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PatchMapping("/cart/items/{cartItemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> updateItemQuantity(@PathVariable Long cartItemId,
                                                      @RequestParam Integer quantity) {
        CartDTO cartDTO = cartService.updateCartItemQuantity(cartItemId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartDTO> clearCart() {
        CartDTO cartDTO = cartService.clearCart();
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }
}
