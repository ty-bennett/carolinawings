package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.*;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Menu;
import com.carolinawings.webapp.model.MenuItem;
import com.carolinawings.webapp.repository.MenuItemRepository;
import com.carolinawings.webapp.repository.MenuRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
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

    public MenuItemServiceImplementation(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public MenuItemResponse getAllMenuItems() {
        List<MenuItem> menuItems = menuItemRepository.findAll();
        if (menuItems.isEmpty())
            throw new APIException("No menu items present");
        List<MenuItemDTO> menuItemDTOS = menuItems.stream()
                .map(menuItem -> modelMapper.map(menuItem, MenuItemDTO.class))
                .toList();

        return new MenuItemResponse(menuItemDTOS);
    }

    @Override
    public MenuItemResponse getAllMenuItemsPaged(Integer pageNumber, Integer pageSize, Long menuId) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<MenuItem> menuItems = menuItemRepository.findAll(pageDetails);
        List<MenuItem> menuItemsPageable = menuItems.getContent();
        if (menuItemsPageable.isEmpty())
            throw new APIException("No menu items present");
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
            throw new APIException("No menu items present");
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
        MenuItem menuItem = modelMapper.map(menuItemDTO, MenuItem.class);
        MenuItem existingMenuItem = menuItemRepository.findByName(menuItem.getName());
        if (existingMenuItem != null)
            throw new APIException("Menu item with the name " + menuItem.getName() + " already exists");
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return modelMapper.map(savedMenuItem, MenuItemDTO.class);
    }

    @Override
    public Optional<MenuItemDTO> getMenuItemById(Long id) {
        Optional<MenuItem> menuItem = menuItemRepository.findById(id);
        return menuItem.map(item -> modelMapper.map(item, MenuItemDTO.class));
    }

    @Override
    public MenuItemDTO deleteMenuItem(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuItemId", Long.valueOf(id)));
        menuItemRepository.delete(menuItem);
        return modelMapper.map(menuItem, MenuItemDTO.class);
    }

    @Override
    public MenuItemDTO updateMenuItem(MenuItemDTO menuItemDTO, Long id) {
        MenuItem existingMenuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuItemId", id));
        MenuItem menuItem = modelMapper.map(menuItemDTO, MenuItem.class);
        menuItem.setId(id);
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return modelMapper.map(updatedMenuItem, MenuItemDTO.class);
    }

    @Override
    public MenuItemDTO addMenuItemToMenu(Long menuId, MenuItemDTO requestMenuItem) {
        //get Menu
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "menuID", menuId));

       //Find existing menu Item (if it exists) by name
        MenuItem menuItem =  menuItemRepository.findByName(requestMenuItem.getName());
        if(menuItem == null) {
            menuItem = new MenuItem();
            menuItem.setName(requestMenuItem.getName());
            menuItem.setDescription(requestMenuItem.getDescription());
            menuItem.setImageURL(requestMenuItem.getImageUrl());
            menuItem.setPrice(new BigDecimal(requestMenuItem.getPrice()));
            menuItem.setCategory(requestMenuItem.getCategory());

            menuItemRepository.save(menuItem);
        }
        Optional<Menu> menuToAdd = menuRepository.findById(menuId);
        List<MenuItem> menuItemList = menuToAdd.get().getMenuItemsList();
        boolean existing = menuItemList.stream().anyMatch(item -> item.getId().equals(requestMenuItem.getId()));
        if (existing)
            throw new APIException("Menu item with the id " + menuItem.getId() + " already exists");
        else {
            menuItemList.add(menuItem);
            menuItemRepository.save(menuItem);
            menuToAdd.get().setMenuItemsList(menuItemList);
            menuRepository.save(menuToAdd.get());
        }
        return modelMapper.map(menuItem, MenuItemDTO.class);
    }


    @Override
    public List<MenuItemDTO> getMenuItemsByMenu(String menuId, Integer pageNumber, Integer pageSize) {
        Long newMenuID = Long.valueOf(menuId);
        Menu menu = menuRepository.findById(newMenuID).orElseThrow(() -> new ResourceNotFoundException("Menu", "menuID", menuId));
        if(menu == null) {
            throw new ResourceNotFoundException("Menu", "menuId", menuId);
        }
        return menu.getMenuItemsList().stream().map(item -> modelMapper.map(item, MenuItemDTO.class)).toList();
    }

    @Override
    public MenuItemDTO deleteMenuItemFromMenu(Long menuId, Long menuItemID)
    {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "menuID", menuId));
        MenuItem menuItem = menuItemRepository.findById(menuItemID).orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuItemID", menuItemID));
        return modelMapper.map(menuItem, MenuItemDTO.class);
    }

    @Override
    public MenuItemDTO updateMenuItemByMenu(@PathVariable Long menuId, @PathVariable Long menuItemId, @Valid @RequestBody MenuItemDTO menuItemDTO)
    {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "menuId", menuId));
        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuItemId", menuItemId));

        MenuItem response = modelMapper.map(menuItemDTO, MenuItem.class);
        response.setMenu(menu);
        menuItemRepository.save(menuItem);
        menuRepository.save(menu);

        return modelMapper.map(response, MenuItemDTO.class) ;
    }
}

