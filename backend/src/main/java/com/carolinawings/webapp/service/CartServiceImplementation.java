package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.*;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.model.*;
import com.carolinawings.webapp.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImplementation implements CartService {

    private final CartRepository cartRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemOptionRepository optionRepository;
    private final MenuItemOptionGroupRepository menuItemOptionGroupRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemOptionRepository cartItemOptionRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CartDTO addMenuItemToCart(AddCartItemDTO cartItemDTO) {
        // 1. Find cart
        Cart cart = cartRepository.findById(cartItemDTO.getCartId())
                .orElseThrow(() -> new APIException("Cart not found with id: " + cartItemDTO.getCartId()));

        // 2. Find menu item
        MenuItem menuItem = menuItemRepository.findById(cartItemDTO.getMenuItemId())
                .orElseThrow(() -> new APIException("MenuItem not found with menuitemID: " + cartItemDTO.getMenuItemId()));

        // 3. Create cart item
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setMenuItem(menuItem);
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setMemos(cartItemDTO.getMemos());

        cartItemRepository.save(cartItem);

        // 4. Add options by group name
        if (cartItemDTO.getSelectedOptionGroups() != null) {
            for (SelectedOptionGroupDTO selectedGroupDTO : cartItemDTO.getSelectedOptionGroups()) {
                String groupName = selectedGroupDTO.getGroupName().trim().toLowerCase();

                MenuItemOptionGroup menuItemOptionGroup = menuItemOptionGroupRepository
                        .findByMenuItem_Id(menuItem.getId()).stream()
                        .filter(g -> g.getOptionGroup().getName().trim().toLowerCase().equals(groupName))
                        .findFirst()
                        .orElseThrow(() -> new APIException("No option group found for: " + selectedGroupDTO.getGroupName()));

                log.info("Adding choice {} to {}", selectedGroupDTO.getSelectedOptionNames(), selectedGroupDTO.getGroupName());

                for (String optionName : selectedGroupDTO.getSelectedOptionNames()) {
                    MenuItemOption option = optionRepository.findByName(optionName)
                            .orElseThrow(() -> new APIException("Option not found with name: " + optionName));

                    CartItemChoice cartItemOption = new CartItemChoice();
                    cartItemOption.setCartItem(cartItem);
                    cartItemOption.setMenuItemOption(option);
                    cartItemOptionRepository.save(cartItemOption);
                }
            }
        }

        // 5. Recalculate total price
        recalculateCartTotal(cart);

        return convertToDTO(cart);
    }

    private void recalculateCartTotal(Cart cart) {
        double total = 0.0;
        for (CartItem item : cart.getCartItems()) {
            double itemPrice = item.getMenuItem().getPrice().doubleValue();

            for (CartItem option : item.getCart().getCartItems()) {
                if (option.getMenuItem().getPrice() != null) {
                    itemPrice += option.getPrice();
                }
            }

            total += itemPrice * item.getQuantity();
        }
        cart.setTotalPrice(total);
        cartRepository.save(cart);
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getId());
        dto.setTotalPrice(cart.getTotalPrice());
        // mapping of cart items can go here
        List<CartItemDTO> cartItems = cart.getCartItems().stream()
                .map(item -> modelMapper.map(item, CartItemDTO.class)).toList();

        return dto;
    }
}
