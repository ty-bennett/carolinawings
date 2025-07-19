package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.AddCartItemDTO;
import com.carolinawings.webapp.dto.CartDTO;
import com.carolinawings.webapp.dto.CartItemDTO;
import com.carolinawings.webapp.dto.MenuItemDTO;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Cart;
import com.carolinawings.webapp.model.CartItem;
import com.carolinawings.webapp.model.MenuItem;
import com.carolinawings.webapp.repository.CartItemRepository;
import com.carolinawings.webapp.repository.CartRepository;
import com.carolinawings.webapp.repository.MenuItemRepository;
import com.carolinawings.webapp.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class CartServiceImplementation implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDTO addMenuItemToCart(@RequestBody AddCartItemDTO cartItemDTO) {
        Cart cart = createCart();
        //Retrieve menu item  dets
        MenuItem menuItem = menuItemRepository.findById(cartItemDTO.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuitemID", cartItemDTO.getMenuItemId()));
        //perform validations (menu item in stock)?
        CartItem cartItem = cartItemRepository.findCartItemByMenuItemIdAndCartId(cart.getId(), cartItemDTO.getMenuItemId());
        if(cartItem != null) {
            throw new APIException("Menu item" + menuItem.getName() + " already exists inside cart");
        }
        if(menuItem.getEnabled() == false){
            throw new APIException("Menu item " + menuItem.getName() + " is unavailable");
        }
        if(cartItemDTO.getQuantity() <= 0) {
            throw new APIException("Quantity must be greater than 0");
        }
        //create cart item
        CartItem newCartItem = new CartItem();

        newCartItem.setMenuItem(menuItem);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(cartItemDTO.getQuantity());
        newCartItem.setMemos(cartItemDTO.getMemos());
        newCartItem.setMenuItemPrice(menuItem.getPrice().doubleValue());
        //save cart item
        cartItemRepository.save(newCartItem);
        // return update cart
        cart.setTotalPrice(cart.getTotalPrice() + (cartItemDTO.getQuantity() * newCartItem.getMenuItemPrice()));
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();

        List<CartItemDTO> cartItemDTOS = cartItems.stream().map(
                item -> {
                    CartItemDTO newCartItemDTO = new CartItemDTO();
                    newCartItemDTO.setCartItemId(item.getId());
                    newCartItemDTO.setQuantity(item.getQuantity());
                    newCartItemDTO.setMemos(item.getMemos());
                    newCartItemDTO.setSauces(item.getSauce());
                    newCartItemDTO.setDressing(item.getDressing());
                    MenuItemDTO menuItemDTO = modelMapper.map(item.getMenuItem(), MenuItemDTO.class);
                    newCartItemDTO.setMenuItem(menuItemDTO);

                    return newCartItemDTO;
                }).toList();
        cartDTO.setMenuItems(cartItemDTOS);

        return cartDTO;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByUserEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }
}
