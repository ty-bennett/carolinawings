package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.*;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Menu;
import com.carolinawings.webapp.model.MenuItem;
import com.carolinawings.webapp.repository.MenuItemRepository;
import com.carolinawings.webapp.repository.MenuRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    public MenuItemResponse getAllMenuItemsPaged(Integer pageNumber, Integer pageSize) {
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
    public MenuItemResponse getAllMenuItemsSorted(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {
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
        mR.setTotalElements(mR.getTotalElements());
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
    public Optional<MenuItemDTO> getMenuItemById(Integer id) {
        Optional<MenuItem> menuItem = menuItemRepository.findById(id);
        return menuItem.map(item -> modelMapper.map(item, MenuItemDTO.class));
    }

    @Override
    public MenuItemDTO deleteMenuItem(Integer id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuItemId", Long.valueOf(id)));
        menuItemRepository.delete(menuItem);
        return modelMapper.map(menuItem, MenuItemDTO.class);
    }

    @Override
    public MenuItemDTO updateMenuItem(MenuItemDTO menuItemDTO, Integer id) {
        MenuItem existingMenuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuItemId", Long.valueOf(id)));
        MenuItem menuItem = modelMapper.map(menuItemDTO, MenuItem.class);
        menuItem.setId(id);
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return modelMapper.map(updatedMenuItem, MenuItemDTO.class);
    }

    public MenuItemDTO addMenuItemToMenu (Long menuId, MenuItemDTO menuItem) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "menuID", menuId));
        List<MenuItemDTO> menuItemDTOS = menu.getMenuItemsList().stream().map((element) -> modelMapper.map(element, MenuItemDTO.class)).toList();
        if(menuItemDTOS.contains(menuItem))
        {
            throw new APIException("Menu item already in list!");
        }
        return modelMapper.map(menuItem, MenuItemDTO.class);
    }

    public MenuItemResponse getMenuItemsByMenu(Long menuId, Integer pageNumber, Integer pageSize) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "menuID", menuId));
        List<MenuItemDTO> responseMenuItem = menu.getMenuItemsList().stream().map((element) -> modelMapper.map(element, MenuItemDTO.class)).toList();
        MenuItemResponse response = new MenuItemResponse();
        response.setContent(responseMenuItem);
        response.setPageSize(pageSize);
        response.setPageNumber(pageNumber);
        return response;
    }

    public MenuItemDTO updateMenuItemByMenu(Long menuId, Integer menuItemID, MenuItem menuItem) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "menuID", menuId));

        MenuItem menuItemToFind = menuItemRepository.findById(menuItemID)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuItemID", Long.valueOf(menuItemID)));

        boolean exists = menu.getMenuItemsList().stream().anyMatch(menuItem1 -> menuItem1.getId().equals(menuItemID));
        MenuItem saved = menuItemRepository.save(menuItem);
        menuRepository.save(menu);
        return modelMapper.map(saved, MenuItemDTO.class);
    }

    public MenuItemDTO deleteMenuItemFromMenu(Long menuId, Integer menuItemID)
    {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "menuID", menuId));
        MenuItem menuItem = menuItemRepository.findById(menuItemID).orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuItemID", menuItemID));
        return modelMapper.map(menuItem, MenuItemDTO.class);
    }
}

