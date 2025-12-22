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
        return null;
    }

    @Override
    public MenuItemResponse getAllMenuItems(Long menuId) {
        List<MenuItem> menuItems = menuItemRepository.findAllByMenu_Id(menuId);
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
        Menu menu = menuRepository.findById(menuId).orElseThrow(ResourceNotFoundException::new);
        Page<MenuItem> menuItems = menuItemRepository.findAllByMenu_Id(menuId, pageDetails);
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
        MenuItem existingMenuItem = menuItemRepository.findByName(menuItem.getName())
                .orElseThrow(ResourceNotFoundException::new);
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
        MenuItem menuItem =  menuItemRepository.findByName(requestMenuItem.getName())
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "menuItemName", requestMenuItem.getName()));
        if(menuItem == null) {
            menuItem = new MenuItem();
            menuItem.setName(requestMenuItem.getName());
            menuItem.setDescription(requestMenuItem.getDescription());
            menuItem.setImageURL(requestMenuItem.getImageUrl());
            menuItem.setPrice(requestMenuItem.getPrice());
            menuItem.setCategory(requestMenuItem.getCategory());
            menuItem.setMenu(menu);

            menuItemRepository.save(menuItem);
        }
        List<MenuItem> menuItemList = menu.getMenuItemsList();
        boolean existing = menuItemList.stream().anyMatch(item -> item.getId().equals(requestMenuItem.getId()));
        if (existing)
            throw new APIException("Menu item with the id " + menuItem.getId() + " already exists");
        else {
            menuItemList.add(menuItem);
            menuItemRepository.save(menuItem);
            menu.setMenuItemsList(menuItemList);
            menuRepository.save(menu);
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
        List<MenuItem> menuItemList = menu.getMenuItemsList();
        MenuItem deletedMenuItem = menuItemList.stream().filter(item -> item.getId().equals(menuItemID)).findFirst().get();
        menuItemList.removeIf(item -> item.getId().equals(menuItemID));
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
}

