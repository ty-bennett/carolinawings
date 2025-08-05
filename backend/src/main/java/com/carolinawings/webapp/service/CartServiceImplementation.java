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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    MenuItemOptionGroupRepository ruleRepository;


    @Override
    public CartDTO addMenuItemToCart(@RequestBody AddCartItemDTO cartItemDTO) {
        Cart cart = createCart();

        MenuItem menuItem = menuItemRepository.findById(cartItemDTO.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuitemID", cartItemDTO.getMenuItemId()));

        CartItem existingItem = cartItemRepository.findCartItemByMenuItemIdAndCartId(cart.getId(), cartItemDTO.getMenuItemId());
        if (existingItem != null) {
            throw new APIException("Menu item " + menuItem.getName() + " already exists inside cart");
        }

        if (!menuItem.getEnabled()) {
            throw new APIException("Menu item " + menuItem.getName() + " is unavailable");
        }

        if (cartItemDTO.getQuantity() <= 0) {
            throw new APIException("Quantity must be greater than 0");
        }
        // ✅ Validate and add sauces
        validateAndAddChoices("sauce", cartItemDTO.getSelectedSauceOptionIds(), menuItem);

        // ✅ Validate and add dressings
//        validateAndAddChoices("dressing", cartItemDTO.getSelectedDressingIds(), menuItem, newCartItem, choices);

        // ✅ Attach choices and save
//        CartDTO returnCart = new CartDTO();
//        returnCart.setCartId(cart.getId());
//        returnCart.setTotalPrice(cart.getTotalPrice() + (cartItemDTO.getQuantity() * menuItem.getPrice().doubleValue()));

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setMenuItem(menuItem);
        newCartItem.setQuantity(cartItemDTO.getQuantity());
        newCartItem.setMemos(cartItemDTO.getMemos());
        newCartItem.setChoices(new ArrayList<>());
        cartItemRepository.save(newCartItem);
        cart.getCartItems().add(newCartItem);
        cartRepository.save(cart);
//        CartItemDTO returnCartItem = modelMapper.map(newCartItem, CartItemDTO.class);

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
        return cartRepository.save(cart);
    }

    private void validateAndAddChoices(String type, List<Long> selectedOptionIds, MenuItem menuItem) {
        if (selectedOptionIds == null) {
            selectedOptionIds = new ArrayList<>();
        }

        MenuItemOptionGroup optionGroup = menuItem.getOptionGroups().stream()
                .filter(group -> type.equalsIgnoreCase(group.getOptionType()))
                .findFirst()
                .orElseThrow(() -> new APIException("Menu item not found with that group: " + menuItem.getOptionGroups().getFirst().getId()));

        if (optionGroup == null) {
            if (!selectedOptionIds.isEmpty()) {
                throw new APIException("No option group for " + type + " expected, but options were provided");
            }
            return; // skip, nothing to validate or add
        }

        int selectedCount = selectedOptionIds.size();

        if (selectedCount == 0) {
            throw new APIException("At least one " + type + " must be selected");
        }

        if (selectedCount < optionGroup.getMinChoices() || selectedCount > optionGroup.getMaxChoices()) {
            throw new APIException("You must select between " + optionGroup.getMinChoices() + " and " + optionGroup.getMaxChoices() + " " + type + "(s)");
        }

        for (Long optionId : selectedOptionIds) {
            MenuItemOption option = optionRepository.findById(optionId)
                    .orElseThrow(() -> new APIException("Option not found: " + optionId));

            if (!optionGroup.getOptionGroup().getOptions().contains(option)) {
                throw new APIException("Option " + optionId + " is not valid for this menu item");
            }
        }
    }
}
