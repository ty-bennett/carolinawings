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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartServiceImplementation cartServiceImplementation;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/carts/menuitems")
    public ResponseEntity<CartDTO> addProductToCart(@RequestBody AddCartItemDTO addCartItemDTO)
    {
        CartDTO cartDTO = cartService.addMenuItemToCart(addCartItemDTO);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartByUser() {
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByUserEmail(emailId);
        Long cartId = cart.getId();
        CartDTO cartDTO = cartServiceImplementation.getUserCart(emailId, cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.FOUND);
    }


}
