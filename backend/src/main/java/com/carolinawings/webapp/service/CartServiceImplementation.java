package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.*;
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
import java.util.List;
import java.util.Set;


@Service
@Slf4j
public class CartServiceImplementation implements CartService {

    private CartRepository cartRepository;
    private MenuItemRepository menuItemRepository;
    private MenuItemOptionRepository optionRepository;
    private MenuItemOptionGroupRepository menuItemOptionGroupRepository;
    private CartItemRepository cartItemRepository;
    private CartItemOptionRepository cartItemOptionRepository;
    private ModelMapper modelMapper;
    private AuthUtil authUtil;

    public CartServiceImplementation(
            CartRepository cartRepository,
            MenuItemRepository menuItemRepository,
            MenuItemOptionRepository optionRepository,
            MenuItemOptionGroupRepository menuItemOptionGroupRepository,
            CartItemRepository cartItemRepository,
            CartItemOptionRepository cartItemOptionRepository,
            ModelMapper modelMapper,
            AuthUtil authUtil
    ) {
        this.cartRepository = cartRepository;
        this.menuItemRepository = menuItemRepository;
        this.optionRepository = optionRepository;
        this.menuItemOptionGroupRepository = menuItemOptionGroupRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartItemOptionRepository = cartItemOptionRepository;
        this.modelMapper = modelMapper;
        this.authUtil = authUtil;
    }
    @Override
    @Transactional
    public CartDTO addMenuItemToCart(AddCartItemDTO cartItemDTO) {
        Cart cart = cartRepository.findCartByUserEmail(authUtil.loggedInEmail());
        if(cart == null) {
            cart = createCart();
        }

        // 2. Find menu item
        MenuItem menuItem = menuItemRepository.findById(cartItemDTO.getMenuItemId())
                .orElseThrow(() -> new APIException("MenuItem not found with menuitemID: " + cartItemDTO.getMenuItemId()));
        log.info(menuItem.toString());
        if(menuItem.getEnabled() == false)
        {
            throw new APIException("Menu item is disabled");
        }

        // 3. Create cart item
        CartItem cartItem = new CartItem();
        cart.getCartItems().add(cartItem);
        cartItem.setCart(cart);
        cartItem.setMenuItem(menuItem);
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setMemos(cartItemDTO.getMemos());
        cartItem.setPrice(menuItem.getPrice().multiply(new BigDecimal(cartItemDTO.getQuantity())));
        log.info(cartItem.toString());
        cartItemRepository.save(cartItem);

        // 4. Add options by group name
        if (cartItemDTO.getSelectedOptionGroups() != null) {
            for (SelectedOptionGroupDTO selectedGroupDTO : cartItemDTO.getSelectedOptionGroups()) {
                MenuItemOptionGroup menuItemOptionGroup = menuItemOptionGroupRepository
                        .findByMenuItem_Id(menuItem.getId())
                        .orElseThrow(() -> new APIException("No option group found for: " + selectedGroupDTO.getGroupName()));
                menuItemOptionGroupRepository.save(menuItemOptionGroup);
                log.info(menuItemOptionGroup.toString());
                log.info("Adding choice {} to {}", selectedGroupDTO.getSelectedOptionNames(), selectedGroupDTO.getGroupName());

                for (String optionName : selectedGroupDTO.getSelectedOptionNames()) {
                    MenuItemOption option = optionRepository.findByName(optionName)
                            .orElseThrow(() -> new APIException("Option not found with name: " + optionName));
                    log.info(option.toString());

                    CartItemChoice cartItemOption = new CartItemChoice();
                    cartItemOption.setCartItem(cartItem);
                    cartItemOption.setMenuItemOption(option);
                    cartItemOption.setChoiceType(option.getName());
                    cartItemOptionRepository.save(cartItemOption);
                    log.info(cartItemOption.toString());
                }
            }
        }

        // 5. Recalculate total price
        BigDecimal recalculatedTotal = recalculateCartTotal(cart);
        cart.setTotalPrice(recalculatedTotal);
        cart = cartRepository.save(cart);

//        Cart fullCart = cartRepository.findByIdWithCartItemsAndChoices(cart.getId())
//                .orElseThrow(() -> new APIException("Cart not found after save"));

//        BigDecimal newTotal = recalculateCartTotal(fullCart);
//        fullCart.setTotalPrice(newTotal);
//        cartRepository.save(fullCart);
//        cartRepository.flush();

        cartRepository.flush();
        cartItemRepository.flush();
        optionRepository.flush();

        Cart updatedCart = cartRepository.findCartByIdWithOptions(cart.getId());

        Set<CartItem> cartItemList = updatedCart.getCartItems();
        if(cartItemList.isEmpty()) {
            log.info("No cart items found");
            throw new APIException("No cart items found");
        }

        log.info(cartItemList.toString());
        log.info("CartItem has {} total price, with {}", updatedCart.getTotalPrice(), updatedCart.getCartItems().toString());

        List<CartItemDTO> cartItemDTOs = cartItemList.stream()
                        .map(this::mapCartItemToDTO)
                        .toList();

        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getId());
        dto.setTotalPrice(cart.getTotalPrice());
        dto.setMenuItems(cartItemDTOs);
        log.info(dto.toString());
        return dto;
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
