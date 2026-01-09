package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.*;
import com.carolinawings.webapp.enums.RoleName;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.CartItem;
import com.carolinawings.webapp.model.Menu;
import com.carolinawings.webapp.model.MenuItem;
import com.carolinawings.webapp.model.User;
import com.carolinawings.webapp.repository.CartItemRepository;
import com.carolinawings.webapp.repository.MenuItemRepository;
import com.carolinawings.webapp.repository.MenuRepository;
import com.carolinawings.webapp.util.AuthUtil;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MenuItemServiceImplementation implements MenuItemService {

    @Autowired
    private final MenuItemRepository menuItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private CartItemRepository cartItemRepository;

    public MenuItemServiceImplementation(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public MenuItemResponse getAllMenuItems() {
        User currentUser = authUtil.loggedInUser();
        List<MenuItem> menuItems = new ArrayList<>();

        if(currentUser.getRoles().stream().anyMatch(role -> role.getName() == RoleName.ADMIN)) {
            menuItems = menuItemRepository.findAll();
        } else if(currentUser.getRoles().stream().anyMatch(
                role -> role.getName() == RoleName.RESTAURANT_ADMIN || role.getName() == RoleName.MANAGER)) {
            Page<MenuItem> pagedItems = menuItemRepository.findAllByMenu_Restaurant_RestaurantAdmin_Id(
                    currentUser.getId(),
                    Pageable.unpaged()
            );
            menuItems = pagedItems.getContent();
        }
        if(menuItems.isEmpty()) {
            return new MenuItemResponse();
        }
        List<MenuItemDTO> menuItemDTOS = menuItems.stream()
                .map(menuItem -> modelMapper.map(menuItem, MenuItemDTO.class))
                .toList();
        return new MenuItemResponse(menuItemDTOS);
    }

    @Override
    public MenuItemResponse getAllMenuItemsPaged(Integer pageNumber, Integer pageSize, Long menuId) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Menu menu = menuRepository.findById(menuId).orElseThrow(ResourceNotFoundException::new);
        Page<MenuItem> menuItems = menuItemRepository.findAllByMenu_Id(menuId, pageDetails);
        List<MenuItem> menuItemsPageable = menuItems.getContent();
        if (menuItemsPageable.isEmpty())
           return new MenuItemResponse();
        List<MenuItemDTO> menuItemDTOS = menuItemsPageable.stream()
                .map(menuItem -> modelMapper.map(menuItem, MenuItemDTO.class))
                .toList();
        MenuItemResponse mR = new MenuItemResponse();
        mR.setContent(menuItemDTOS);
        mR.setPageNumber(menuItems.getNumber());
        mR.setPageSize(menuItems.getSize());
        mR.setTotalPages(menuItems.getTotalPages());
        mR.setTotalElements(mR.getTotalElements());
        mR.setLastPage(mR.isLastPage());
        return mR;
    }

    @Override
    public MenuItemResponse getAllMenuItemsSorted(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection, Long menuId) {
        Sort sortByAndOrder = sortDirection.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<MenuItem> menuItems = menuItemRepository.findAll(pageDetails);
        List<MenuItem> menuItemsPageable = menuItems.getContent();
        if (menuItemsPageable.isEmpty())
            return new MenuItemResponse();
        List<MenuItemDTO> menuItemDTOS = menuItemsPageable.stream()
                .map(menuItem -> modelMapper.map(menuItem, MenuItemDTO.class))
                .toList();

        MenuItemResponse mR = new MenuItemResponse();
        mR.setContent(menuItemDTOS);
        mR.setPageNumber(menuItems.getNumber());
        mR.setPageSize(menuItems.getSize());
        mR.setTotalPages(menuItems.getTotalPages());
        mR.setTotalElements(menuItems.getTotalElements());
        mR.setLastPage(mR.isLastPage());
        return mR;
    }

    @Override
    public MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO) {

        Optional<MenuItem> existingMenuItem = menuItemRepository.findByName(menuItemDTO.getName());

        if(existingMenuItem.isPresent()) {
            throw new APIException("Item already exists");
        }

        MenuItem menuItem = new MenuItem();
        menuItem.setName(menuItemDTO.getName());
        menuItem.setDescription(menuItemDTO.getDescription());
        menuItem.setPrice(menuItemDTO.getPrice());
        menuItem.setCategory(menuItemDTO.getCategory());
        menuItem.setImageURL(menuItemDTO.getImageUrl());

        MenuItem savedItem = menuItemRepository.save(menuItem);
        return modelMapper.map(savedItem, MenuItemDTO.class);
    }

    @Override
    public Optional<MenuItemDTO> getMenuItemById(Long id) {
        Optional<MenuItem> menuItem = menuItemRepository.findById(id);
        return menuItem.map(item -> modelMapper.map(item, MenuItemDTO.class));
    }

    @Override
    public MenuItemDTO deleteMenuItem(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuItemId", id));

        // Remove cart items that reference this menu item
        List<CartItem> cartItems = cartItemRepository.findByMenuItemId(id);
        for (CartItem cartItem : cartItems) {
            cartItem.setMenuItem(null);
            cartItemRepository.delete(cartItem);
        }

        // Remove from menu's list
        Menu menu = menuItem.getMenu();
        if (menu != null) {
            menu.getMenuItemsList().remove(menuItem);
        }

        // Clear option groups
        menuItem.getOptionGroups().clear();
        menuItemRepository.save(menuItem);

        // Now delete
        menuItemRepository.delete(menuItem);

        return modelMapper.map(menuItem, MenuItemDTO.class);
    }

    @Override
    public MenuItemDTO updateMenuItem(MenuItemDTO menuItemDTO, Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuItemId", id));

        // Update fields directly
        if (menuItemDTO.getName() != null) {
            menuItem.setName(menuItemDTO.getName());
        }
        if (menuItemDTO.getDescription() != null) {
            menuItem.setDescription(menuItemDTO.getDescription());
        }
        if (menuItemDTO.getPrice() != null) {
            menuItem.setPrice(menuItemDTO.getPrice());
        }
        if (menuItemDTO.getCategory() != null) {
            menuItem.setCategory(menuItemDTO.getCategory());
        }
        if (menuItemDTO.getEnabled() != null) {
            menuItem.setEnabled(menuItemDTO.getEnabled());
        }
        if (menuItemDTO.getImageUrl() != null) {
            menuItem.setImageURL(menuItemDTO.getImageUrl());
        }

        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return modelMapper.map(updatedMenuItem, MenuItemDTO.class);
    }

    @Override
    public MenuItemDTO addMenuItemToMenu(Long menuId, MenuItemDTO requestMenuItem) {
        //get Menu
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "menuID", menuId));

       //Find existing menu Item (if it exists) by name

        boolean alreadyExists = menu.getMenuItemsList().stream()
                .anyMatch(item -> item.getName().equalsIgnoreCase(requestMenuItem.getName()));

        if(alreadyExists) {
            throw new APIException("Menu item with the name " + requestMenuItem.getName() + " already exists in this menu");
        }

        MenuItem menuItem = new MenuItem();
        menuItem.setName(requestMenuItem.getName());
        menuItem.setDescription(requestMenuItem.getDescription());
        menuItem.setImageURL(requestMenuItem.getImageUrl());
        menuItem.setPrice(requestMenuItem.getPrice());
        menuItem.setCategory(requestMenuItem.getCategory());
        menuItem.setMenu(menu);
        MenuItem saved = menuItemRepository.save(menuItem);

        List<MenuItem> menuItemList = menu.getMenuItemsList();
        menuItemList.add(menuItem);
        menu.setMenuItemsList(menuItemList);
        menuRepository.save(menu);

        return modelMapper.map(saved, MenuItemDTO.class);
    }


    @Override
    public List<MenuItemDTO> getMenuItemsByMenu(String menuId, Integer pageNumber, Integer pageSize) {
        Long newMenuID = Long.valueOf(menuId);
        Menu menu = menuRepository.findById(newMenuID).orElseThrow(() -> new ResourceNotFoundException("Menu", "menuID", menuId));
        return menu.getMenuItemsList().stream().map(item -> modelMapper.map(item, MenuItemDTO.class)).toList();
    }

    @Override
    public MenuItemDTO deleteMenuItemFromMenu(Long menuId, Long menuItemId)
    {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "menuID", menuId));
        List<MenuItem> menuItemList = menu.getMenuItemsList();
        MenuItem deletedMenuItem = menuItemList.stream().filter(item -> item.getId().equals(menuItemId)).findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Menu", "menuItemId", menuItemId));
        menuItemList.removeIf(item -> item.getId().equals(menuItemId));
        menu.setMenuItemsList(menuItemList);
        menuRepository.save(menu);
        return modelMapper.map(deletedMenuItem, MenuItemDTO.class);
    }

    public MenuItemDTO editMenuItemByMenu(Long id, Long menuItemId, @Valid MenuItemDTO menuItemDTO)
    {
        Menu menu = menuRepository.findById(id).orElseThrow(()-> new RuntimeException("Menu not found"));
        MenuItem menuItem = menu.getMenuItemsList().stream()
                .filter(item -> item.getId()
                .equals(menuItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("MenuItem not found"));

        menuItem.setName(menuItemDTO.getName());
        menuItem.setDescription(menuItemDTO.getDescription());
        menuItem.setImageURL(menuItemDTO.getImageUrl());
        menuItem.setPrice(menuItemDTO.getPrice());
        menuItem.setCategory(menuItemDTO.getCategory());
        menuItem.setEnabled(menuItemDTO.getEnabled());
        menuItem.setMenu(menu);
        MenuItem res = menuItemRepository.save(menuItem);
        menuRepository.save(menu);
        return modelMapper.map(res, MenuItemDTO.class);
    }

    public MenuItemDTO cloneLibraryItemToMenu(Long menuId, Long libraryItemId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "menuId", menuId));

        MenuItem libraryItem = menuItemRepository.findById(libraryItemId)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuItemId", libraryItemId));

        boolean exists = menu.getMenuItemsList().stream()
                .anyMatch(item -> item.getName().equalsIgnoreCase(libraryItem.getName()));
        if(exists) {
            throw new APIException("Menu already has a menu item named '" + libraryItem.getName() + "' ");
        }

        // Create a copy
        MenuItem cloned = new MenuItem();
        cloned.setName(libraryItem.getName());
        cloned.setDescription(libraryItem.getDescription());
        cloned.setImageURL(libraryItem.getImageURL());
        cloned.setPrice(libraryItem.getPrice());
        cloned.setCategory(libraryItem.getCategory());
        cloned.setEnabled(libraryItem.getEnabled());
        cloned.setMenu(menu);

        MenuItem saved = menuItemRepository.save(cloned);
        return modelMapper.map(saved, MenuItemDTO.class);
    }

    @Override
    public MenuItemResponse getMenuItemsWithoutMenu() {
        List<MenuItem> libraryItems = menuItemRepository.findByMenuIsNull();

        if (libraryItems.isEmpty()) {
            throw new APIException("No library items found");
        }

        List<MenuItemDTO> dtos = libraryItems.stream()
                .map(item -> modelMapper.map(item, MenuItemDTO.class))
                .toList();

        return new MenuItemResponse(dtos);
    }
}

