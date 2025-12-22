package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.*;
import com.carolinawings.webapp.enums.CartStatus;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.model.*;
import com.carolinawings.webapp.repository.*;
import com.carolinawings.webapp.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


@Service
@Slf4j
public class CartServiceImplementation implements CartService {

    private final CartRepository cartRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemOptionRepository optionRepository;
    private final MenuItemOptionGroupRepository menuItemOptionGroupRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemOptionRepository cartItemOptionRepository;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;
    private final MenuItemOptionRepository menuItemOptionRepository;

    public CartServiceImplementation(
            CartRepository cartRepository,
            MenuItemRepository menuItemRepository,
            MenuItemOptionRepository optionRepository,
            MenuItemOptionGroupRepository menuItemOptionGroupRepository,
            CartItemRepository cartItemRepository,
            CartItemOptionRepository cartItemOptionRepository,
            ModelMapper modelMapper,
            AuthUtil authUtil,
            MenuItemOptionRepository menuItemOptionRepository) {
        this.cartRepository = cartRepository;
        this.menuItemRepository = menuItemRepository;
        this.optionRepository = optionRepository;
        this.menuItemOptionGroupRepository = menuItemOptionGroupRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartItemOptionRepository = cartItemOptionRepository;
        this.modelMapper = modelMapper;
        this.authUtil = authUtil;
        this.menuItemOptionRepository = menuItemOptionRepository;
    }
    @Override
    @Transactional
    public CartDTO addMenuItemToCart(AddCartItemDTO cartItemDTO) {
        Cart cart = cartRepository.findCartByUserEmail(authUtil.loggedInEmail());
        if (cart == null) {
            cart = createCart();
        }

        // 2. Find menu item
        MenuItem menuItem = menuItemRepository.findById(cartItemDTO.getMenuItemId())
                .orElseThrow(() -> new APIException("MenuItem not found with menuitemID: " + cartItemDTO.getMenuItemId()));
        log.info(menuItem.toString());
        if (menuItem.getEnabled() == false) {
            throw new APIException("Menu item is disabled");
        }
        CartItem cartItem = new CartItem();
        cart.getCartItems().add(cartItem);
        cartItem.setCart(cart);
        cartItem.setMenuItem(menuItem);
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setMemos(cartItemDTO.getMemos());
        cartItem.setPrice(menuItem.getPrice().multiply(new BigDecimal(cartItemDTO.getQuantity())));
        log.info(cartItem.toString());
        cartItemRepository.save(cartItem);

        // get each option translated over

        Map<MenuItemOptionGroup, List<MenuItemOption>> selectionsByGroup = new HashMap<>();

        for (SelectedOptionGroupDTO groupDTO : cartItemDTO.getSelectedOptionGroups()) {
            MenuItemOptionGroup menuItemOptionGroup = menuItemOptionGroupRepository
                    .findByMenuItemIdAndOptionGroupId(
                            menuItem.getId(),
                            groupDTO.getOptionGroupId()
                    )
                    .orElseThrow(() -> new APIException("Invalid option group for this menu item")
                    );

            List<MenuItemOption> selectedOptions = new ArrayList<>();

            for (Long optionId : groupDTO.getSelectedOptionIds()) {
                MenuItemOption option = menuItemOptionRepository
                        .findById(optionId)
                        .orElseThrow(() ->
                                new APIException("Invalid option selected")
                        );

                if (!option.getOptionGroup().getId().equals(menuItemOptionGroup.getId())) {
                    throw new APIException("Option does not belong to option group");
                }

                selectedOptions.add(option);
            }
            selectionsByGroup.put(menuItemOptionGroup, selectedOptions);
        }

        List<MenuItemOptionGroup> optionGroups =
                menuItemOptionGroupRepository.findByMenuItem(menuItem);

        for (MenuItemOptionGroup group : optionGroups) {
            int selectedQuantity = selectionsByGroup.getOrDefault(group, List.of()).size();

            int defaultCount = optionRepository
                    .findByOptionGroup(group.getOptionGroup())
                    .stream()
                    .filter(MenuItemOption::isDefaultSelected)
                    .toList()
                    .size();

            int effectiveCount = selectedQuantity + defaultCount;

            if (group.isRequired() && effectiveCount == 0) {
                throw new APIException("" +
                        "Option group " + group.getOptionGroup().getName() + " is required"
                );
            }

            if (group.getMinChoices() > 0 && effectiveCount < group.getMinChoices()) {
                throw new APIException(
                        "Must select at least " + group.getMinChoices() +
                                " option(s) for '" + group.getOptionGroup().getName() + "'"
                );
            }

            if (group.getMaxChoices() > 0 && effectiveCount > group.getMaxChoices()) {
                throw new APIException(
                        "Cannot select more than " + group.getMaxChoices() +
                                " option(s) for '" + group.getOptionGroup().getName() + "'"
                );
            }
        }

        for (Map.Entry<MenuItemOptionGroup, List<MenuItemOption>> entry : selectionsByGroup.entrySet()) {

            for (MenuItemOption option : entry.getValue()) {
                CartItemChoice choice = CartItemChoice.builder()
                        .cartItem(cartItem)
                        .menuItemOption(option)
                        .choiceType(entry.getKey().getOptionGroup().getName())
                        .build();

                cartItem.getChoices().add(choice);
            }
        }
        cartItemRepository.save(cartItem);

        // 5. Recalculate total price
        BigDecimal recalculatedTotal = recalculateCartTotal(cart);
        cart.setTotalPrice(recalculatedTotal);
        cart = cartRepository.save(cart);

        Cart updatedCart = cartRepository.findCartByIdWithOptions(cart.getId());

        Set<CartItem> cartItemList = updatedCart.getCartItems();
        if (cartItemList.isEmpty()) {
            log.info("No cart items found");
            throw new APIException("No cart items found");
        }
        List<CartItemDTO> cartItemDTOs = cartItemList.stream()
                .map(this::mapCartItemToDTO)
                .toList();
        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getId());
        dto.setTotalPrice(cart.getTotalPrice());
        dto.setMenuItems(cartItemDTOs);
        log.info(dto.toString());
        return dto;
//        } else {
//            throw new APIException("Invalid menu item");
//        }
    }

    public CartDTO getUserCart(String userEmail, Long cartId) {
        Cart cart = cartRepository.findCartByUser_UsernameAndCartStatusOrderByIdDesc(
                        authUtil.loggedInEmail(), CartStatus.ACTIVE)
                .orElseGet(this::createCart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItemList = cart.getCartItems().stream().toList();
        List<CartItemDTO> cartItemDTOs = cartItemList.stream().map(item -> mapCartItemToDTO(item)).toList();
        cartDTO.setMenuItems(cartItemDTOs);
        return cartDTO;
    }

    private BigDecimal recalculateCartTotal(Cart cart) {
        BigDecimal total = BigDecimal.ZERO;
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        for (CartItem item : cartItems){
            // Start with base menu item price
            BigDecimal itemPrice = item.getMenuItem().getPrice() != null
                    ? item.getMenuItem().getPrice()
                    : BigDecimal.ZERO;

            // Fetch related choices for this cart item
            List<CartItemChoice> choices = cartItemOptionRepository.findByCartItem(item);

            // Add prices for each option
            for (CartItemChoice choice : choices) {
                if (choice.getMenuItemOption() != null && choice.getMenuItemOption().getPrice() != null) {
                    itemPrice = itemPrice.add(choice.getMenuItemOption().getPrice());
                }
            }

            // Multiply by quantity
            total = total.add(itemPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        cart.setTotalPrice(total);
        cartRepository.save(cart);
        cartRepository.flush();
        return total;
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getId());
        dto.setTotalPrice(cart.getTotalPrice());
        log.info("CartItem has {} total price, with {}", cart.getTotalPrice(), cart.getCartItems().toString());
        List<CartItemDTO> cartItemDTOs = cart.getCartItems().stream()
                .map(this::mapCartItemToDTO)
                .toList();
        dto.setMenuItems(cartItemDTOs);
        return dto;
    }

    private CartItemDTO mapCartItemToDTO(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setCartItemId(cartItem.getId());
        dto.setQuantity(cartItem.getQuantity());
        dto.setMemos(cartItem.getMemos());
        dto.setPrice(cartItem.getPrice() != null ? cartItem.getPrice() : BigDecimal.ZERO);

        // Map MenuItem
        dto.setMenuItem(mapMenuItemToDTO(cartItem.getMenuItem()));

        // Map selected options (sauces, dressings, etc.) as list of option names
        List<String> selectedOptionNames = cartItem.getChoices().stream()
                .map(choice -> choice.getMenuItemOption().getName())
                .toList();
        dto.setSauces(selectedOptionNames);
        return dto;
    }

    private MenuItemDTO mapMenuItemToDTO(MenuItem menuItem) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setId(menuItem.getId());
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setImageUrl(menuItem.getImageURL());
        dto.setPrice(menuItem.getPrice() != null ? menuItem.getPrice() : BigDecimal.ZERO);
        dto.setCategory(menuItem.getCategory());
        dto.setEnabled(menuItem.getEnabled());

        // Map option groups (entities to DTOs)
        List<MenuItemOptionGroupDTO> optionGroupDTOs = menuItem.getOptionGroups().stream()
                .map(this::mapMenuItemOptionGroupToDTO)
                .toList();

        dto.setOptionGroups(optionGroupDTOs);

        return dto;
    }

    private MenuItemOptionGroupDTO mapMenuItemOptionGroupToDTO(MenuItemOptionGroup group) {
        MenuItemOptionGroupDTO dto = new MenuItemOptionGroupDTO();
        dto.setId(group.getId() != null ? group.getId().toString() : null);
        dto.setOptionType(group.getOptionType());
        dto.setMinChoices(group.getMinChoices() > 0 ? group.getMinChoices() : 0);
        dto.setMaxChoices(group.getMaxChoices() > 0 ? group.getMaxChoices() : 0);

        // Map option group entity to DTO (no circular menuItem)
        OptionGroup optionGroupEntity = group.getOptionGroup();
        OptionGroupDTO optionGroupDTO = new OptionGroupDTO();
        optionGroupDTO.setId(optionGroupEntity.getId() != null ? optionGroupEntity.getId().toString() : null);
        optionGroupDTO.setName(optionGroupEntity.getName());

        // Map options inside grouItems
        List<MenuItemOptionDTO> optionDTOs = optionGroupEntity.getOptions().stream()
                .map(this::mapMenuItemOptionToDTO)
                .toList();

        optionGroupDTO.setOptions(optionDTOs);
        dto.setOptionGroup(optionGroupDTO);

        return dto;
    }

    private MenuItemOptionDTO mapMenuItemOptionToDTO(MenuItemOption option) {
        MenuItemOptionDTO dto = new MenuItemOptionDTO();
        dto.setId(option.getId() != null ? option.getId().toString() : null);
        dto.setName(option.getName());

        // Map option's group without exposing entity
        OptionGroup group = option.getOptionGroup();
        OptionGroupDTO groupDTO = new OptionGroupDTO();
        groupDTO.setId(group.getId() != null ? group.getId().toString() : null);
        groupDTO.setName(group.getName());
        dto.setGroup(groupDTO);

        return dto;
    }



    private Cart createCart() {
       Cart userCart = cartRepository.findCartByUserEmail(authUtil.loggedInEmail());
       if (userCart != null) {
          return userCart;
       }

       Cart cart = new Cart();
       cart.setTotalPrice(new BigDecimal(0.0));
       cart.setUser(authUtil.loggedInUser());
       Cart newCart = cartRepository.save(cart);
       return newCart;
    }
}
