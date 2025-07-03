package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.*;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Menu;
import com.carolinawings.webapp.model.MenuItem;
import com.carolinawings.webapp.model.MenuMenuItem;
import com.carolinawings.webapp.repository.MenuItemRepository;
import com.carolinawings.webapp.repository.MenuMenuItemRepository;
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
    private final MenuMenuItemRepository menuMenuItemRepository;

    public MenuItemServiceImplementation(MenuItemRepository menuItemRepository, MenuMenuItemRepository menuMenuItemRepository) {
        this.menuItemRepository = menuItemRepository;
        this.menuMenuItemRepository = menuMenuItemRepository;
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

        Optional<MenuMenuItem> existing = menuMenuItemRepository.findByMenuIdAndMenuItemId(menuId, menuItem.getId());
        if (existing.isPresent())
            throw new APIException("Menu item with the id " + menuItem.getId() + " already exists");

        MenuMenuItem join = new MenuMenuItem();
        join.setMenu(menu);
        join.setMenuItem(menuItem);
        join.setStatus(requestMenuItem.getEnabled() != null ? requestMenuItem.getEnabled() : "enabled");

        menuMenuItemRepository.save(join);

        MenuItemDTO responseDTO = modelMapper.map(menuItem, MenuItemDTO.class);
        responseDTO.setEnabled(join.getStatus());
        return responseDTO;
    }


    @Override
    public MenuDTO getMenuItemsByMenu(Long menuId, Integer pageNumber, Integer pageSize) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "menuID", menuId));

       List<MenuMenuItem> menuItems = menuMenuItemRepository.findAllByMenuId(menuId);
        List<MenuItemDTO> response = menuItems.stream()
                .map(menuItem -> new MenuItemDTO(
                        menuItem.getMenuItem().getName(),
                        menuItem.getMenuItem().getDescription(),
                        menuItem.getMenuItem().getImageURL(),
                        menuItem.getMenuItem().getPrice().toPlainString(),
                        menuItem.getMenuItem().getCategory(),
                        menuItem.getStatus()
                        ))
                .toList();
        MenuDTO menuDTO = modelMapper.map(menu, MenuDTO.class);
        menuDTO.setMenuItemsList(response);
        menuDTO.setName(menu.getName());
        menuDTO.setDescription(menu.getDescription());
        return menuDTO;
    }

    @Override
    public MenuItemDTO deleteMenuItemFromMenu(Long menuId, Integer menuItemID)
    {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "menuID", menuId));
        MenuItem menuItem = menuItemRepository.findById(menuItemID).orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuItemID", menuItemID));
        return modelMapper.map(menuItem, MenuItemDTO.class);
    }

    @Override
    public MenuItemDTO updateMenuItemByMenu(@PathVariable Long menuId, @PathVariable Integer menuItemId, @Valid @RequestBody MenuItemDTO menuItemDTO)
    {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "menuId", menuId));
        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuItemId", menuItemId));
        MenuMenuItem menuMenuItem = menuMenuItemRepository.findByMenuIdAndMenuItemId(menuId, menuItemId).orElseThrow(() -> new ResourceNotFoundException("JoinTable", "MenuID and menuItemId "+ menuId, menuItemId));

        MenuItem response = modelMapper.map(menuItemDTO, MenuItem.class);

        menuMenuItem.setMenu(menu);
        menuMenuItem.setMenuItem(response);
        menuMenuItemRepository.save(menuMenuItem);
        return modelMapper.map(response, MenuItemDTO.class) ;
    }
}

