package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.*;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.*;
import com.carolinawings.webapp.repository.*;
import com.carolinawings.webapp.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LoggingSystemFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImplementation implements CartService {
    private static final Logger logger = LoggerFactory.getLogger(CartServiceImplementation.class);
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

        // ✅ Attach choices and save
        CartDTO returnCart = new CartDTO();
        returnCart.setCartId(cart.getId());
        returnCart.setTotalPrice(cart.getTotalPrice() + (cartItemDTO.getQuantity() * menuItem.getPrice().doubleValue()));

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setMenuItem(menuItem);
        newCartItem.setQuantity(cartItemDTO.getQuantity());
        newCartItem.setMemos(cartItemDTO.getMemos());
        newCartItem.setChoices(new ArrayList<>());

        validateAndAddChoices(newCartItem, menuItem, cartItemDTO.getSelectedOptionGroups());
        cartItemRepository.save(newCartItem);
        cart.getCartItems().add(newCartItem);
        cartRepository.save(cart);

        return returnCart;
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
    private void validateAndAddChoices(
            CartItem newCartItem,
            MenuItem menuItem,
            List<SelectedOptionGroupDTO> selectedGroups
    ) {
        for (SelectedOptionGroupDTO groupDTO : selectedGroups) {
            String groupName = groupDTO.getGroupName();
            List<String> selectedNames = groupDTO.getSelectedOptionNames();
            logger.info("Adding choice " + selectedNames + " to " + groupName);

            // 1. Find the MenuItemOptionGroup for this menuItem by group name
            menuItem.getOptionGroups().stream().forEach(optionGroup -> { logger.info(optionGroup.getOptionGroup().getName()); });
            MenuItemOptionGroup itemOptionGroup = menuItem.getOptionGroups().stream()
                    .filter(g -> g.getOptionGroup().getName().equalsIgnoreCase(groupName))
                    .findFirst()
                    .orElseThrow(() -> {
                        logger.debug(groupName + "does not exist");
                        logger.error("Group not found with name " + groupName);
                        return new APIException("No option group found for: " + groupName);
                    });

            // 2. Get allowed options from the OptionGroup
            List<MenuItemOption> allowedOptions = itemOptionGroup.getOptionGroup().getOptions();

            // 3. Find matching selected options
            List<MenuItemOption> matchedOptions = allowedOptions.stream()
                    .filter(opt -> selectedNames.contains(opt.getName()))
                    .toList();

            // 4. Validate count
            int selectedCount = matchedOptions.size();
            if (itemOptionGroup.isRequired() && selectedCount == 0) {
                throw new APIException("You must select at least one option from: " + groupName);
            }

            if (itemOptionGroup.getMaxChoices() != -1 &&
                    selectedCount > itemOptionGroup.getMaxChoices()) {
                throw new APIException("Too many options selected for " + groupName + ". Max: " + itemOptionGroup.getMaxChoices());
            }

            // 5. Attach each Option as CartItemOption
            for (MenuItemOption option : matchedOptions) {
                CartItemMenuItemOption cartItemOption = new CartItemMenuItemOption();
                cartItemOption.setCartItem(newCartItem);
                cartItemOption.setMenuItemOption(option);
                newCartItem.getChoices().add(cartItemOption);
            }
        }
    }

}
