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
import java.util.Optional;
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

        MenuItemOptionRule rule = ruleRepository.findByMenuItem_IdAndOptionTypeAndQuantity(menuItem.getId(), "sauce", cartItemDTO.getQuantity())
                .orElseThrow(() -> new APIException("No rules match this menu item and quantity"));

        int selectedCount = cartItemDTO.getSelectedSauceOptionIds() != null ? cartItemDTO.getSelectedSauceOptionIds().size() : 0;

        if(selectedCount < rule.getMinChoices() || selectedCount > rule.getMaxChoices()) {
            throw new APIException("Invalid number of sauces: expected " + rule.getMinChoices() + " or " + rule.getMaxChoices());
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

            if(!"sauce".equals(option.getType())) {
                throw new APIException("Option type is not sauce");
            }

            CartItemChoice cartItemChoice = new CartItemChoice();
            cartItemChoice.setCartItem(newCartItem);
            cartItemChoice.setMenuItemOption(option);
            cartItemChoice.setChoiceType(option.getType());
            choices.add(cartItemChoice);
        }

        cartItem.setChoices(choices);
        cart.getCartItems().add(cartItem);
        cartRepository.save(cart);

        return modelMapper.map(cart, CartDTO.class);
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
