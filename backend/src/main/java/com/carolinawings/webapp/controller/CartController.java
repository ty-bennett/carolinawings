package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.dto.AddCartItemDTO;
import com.carolinawings.webapp.dto.CartDTO;
import com.carolinawings.webapp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/carts/menuitems")
    public ResponseEntity<CartDTO> addProductToCart(@RequestBody AddCartItemDTO addCartItemDTO)
    {
        CartDTO cartDTO = cartService.addMenuItemToCart(addCartItemDTO);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }


}
