package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.AddCartItemDTO;
import com.carolinawings.webapp.dto.CartDTO;
import com.carolinawings.webapp.dto.CartItemDTO;
import com.carolinawings.webapp.dto.MenuItemDTO;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.*;
import com.carolinawings.webapp.repository.*;
import com.carolinawings.webapp.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
    @Autowired
    private MenuItemOptionRepository optionRepository;
    @Autowired
    MenuItemOptionRuleRepository ruleRepository;


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
        if(!menuItem.getEnabled()){
            throw new APIException("Menu item " + menuItem.getName() + " is unavailable");
        }
        if(cartItemDTO.getQuantity() <= 0) {
            throw new APIException("Quantity must be greater than 0");
        }

        int quantity = cartItemDTO.getQuantity();

        List<MenuItemOptionRule> rules = ruleRepository.findByMenuItem_IdAndOptionType(menuItem.getId(), "sauce");
        MenuItemOptionRule ruleMatched = rules.stream()
                .filter(r -> quantity >= r.getMinQuantity() && quantity <= r.getMaxQuantity())
                .findFirst()
                .orElseThrow(() -> new APIException("no rule matches this quantity"));
        if(cartItemDTO.getSelectedSauceOptionIds().size() > ruleMatched.getMaxChoices()) {
            throw new APIException("Too many sauces selected. Max allowed: "+ ruleMatched.getMaxChoices());
        }
        //create cart item
        CartItem newCartItem = new CartItem();

        newCartItem.setCart(cart);
        newCartItem.setMenuItem(menuItem);
        newCartItem.setQuantity(cartItemDTO.getQuantity());
        newCartItem.setMemos(cartItemDTO.getMemos());

        List<CartItemChoice> choices = new ArrayList<>();
        for(Long optionId : cartItemDTO.getSelectedSauceOptionIds()) {
            MenuItemOption option = optionRepository.findById(optionId)
                    .orElseThrow(() -> new APIException("Option not found"));
        }





        //save cart item
        cartItemRepository.save(newCartItem);
        cart.getCartItems().add(newCartItem);
        // return update cart
        cart.setTotalPrice(cart.getTotalPrice() + (cartItemDTO.getQuantity() * newCartItem.getMenuItem().getPrice().doubleValue()));
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();
        Stream<CartItemDTO> cartItemDTOS = cartItems.stream().map(
                item -> {
                    CartItemDTO newCartItemDTO = new CartItemDTO();
                    newCartItemDTO.setCartItemId(item.getId());
                    newCartItemDTO.setQuantity(item.getQuantity());
                    newCartItemDTO.setMemos(item.getMemos());
                    //newCartItemDTO.setSauces(item.getSauces());
                    //newCartItemDTO.setDressing(item.getDressing());

                    MenuItemDTO menuItemDTO = modelMapper.map(item.getMenuItem(), MenuItemDTO.class);
                    newCartItemDTO.setMenuItem(menuItemDTO);

                    return newCartItemDTO;
                });
        cartDTO.setMenuItems(cartItemDTOS.toList());
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
