/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.MenuDTO;
import com.carolinawings.webapp.dto.MenuItemDTO;
import com.carolinawings.webapp.dto.MenuResponse;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Menu;
import com.carolinawings.webapp.model.Restaurant;
import com.carolinawings.webapp.repository.MenuItemRepository;
import com.carolinawings.webapp.repository.MenuRepository;
import com.carolinawings.webapp.repository.RestaurantRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenuServiceImplementation implements MenuService {

    private final MenuRepository menuRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MenuItemServiceImplementation menuItemServiceImplementation;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;

    public MenuServiceImplementation(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public MenuResponse getAllMenusByRestaurant(Integer pageNumber, Integer pageSize, Long restaurantId) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new APIException("Restaurant not found"));
        Page<Menu> menus = menuRepository.findByRestaurant_Id(restaurant.getId(), pageDetails);

        List<Menu> menusPageable = menus.getContent();
        List<MenuDTO> menuDTOS = new ArrayList<>();
        for (Menu menuPage : menusPageable) {
            List<MenuItemDTO> menuItemDTOS = menuItemRepository.findAllByMenu_Id(menuPage.getId()).stream().map(item -> modelMapper.map(item, MenuItemDTO.class)).toList();
            MenuDTO dto = new MenuDTO();
            dto.setId(menuPage.getId());
            dto.setName(menuPage.getName());
            dto.setDescription(menuPage.getDescription());
            dto.setMenuItemsList(menuItemDTOS);
            dto.setIsPrimary(menuPage.getIsPrimary());

            if (menuPage.getRestaurant() != null) {
                dto.setRestaurantId(menuPage.getRestaurant().getId());
            }
            menuDTOS.add(dto);
        }
        if (menusPageable.isEmpty())
            throw new APIException("No menus present");

        MenuResponse mR = new MenuResponse();
        mR.setContent(menuDTOS);
        mR.setPageNumber(menus.getNumber());
        mR.setPageSize(menus.getSize());
        mR.setTotalElements(menus.getTotalElements());
        mR.setTotalPages(menus.getTotalPages());
        mR.setLastPage(menus.isLast());
        return mR;
    }

    @Override
    public MenuDTO createMenu(MenuDTO menuDTO) {
        Menu menu = modelMapper.map(menuDTO, Menu.class);
        Menu savedMenu = menuRepository.findByName(menu.getName()).orElseThrow(() -> new APIException("Menu with name "+ menu.getName() + " already exists!"));
        Menu returnMenu = menuRepository.save(menu);
        return modelMapper.map(returnMenu, MenuDTO.class);
    }

    @Override
    public Optional<MenuDTO> getMenuById(Long id) {
        Optional<Menu> menu = menuRepository.findById(id);
        return menu.map(element -> modelMapper.map(element, MenuDTO.class));
    }

    @Override
    public MenuDTO deleteMenu(Long id) {
        Menu m = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "menuId", id));
        menuRepository.delete(m);
        return modelMapper.map(m, MenuDTO.class);
    }

    @Override
    public MenuDTO updateMenu(MenuDTO menuDTO, Long id) {
        Menu savedMenu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "menuId: ", id));
        Menu menu = modelMapper.map(menuDTO, Menu.class);
        menu.setId(id);
        Menu savedMenuToRepo = menuRepository.save(menu);
        return modelMapper.map(savedMenuToRepo, MenuDTO.class);
    }

    @Override
    public MenuDTO getMenuByIdAndRestaurantId(Long restaurantId, Long menuId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "menuId", menuId));
        menu.setRestaurant(restaurant);
        Menu saved = menuRepository.save(menu);
        return modelMapper.map(saved, MenuDTO.class);
    }

    @Override
    public MenuDTO createMenuByRestaurant(Long restaurantId, MenuDTO menuDTO) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));
        boolean exists = restaurant.getMenus().stream().anyMatch(menu -> menu.getName().equals(menuDTO.getName()));
        if(exists) {
            throw new APIException("Menu with name "+ menuDTO.getName() + " already exists!");
        }
        List<Menu> menuList = restaurant.getMenus().stream().toList();
        menuList.stream().forEach(element -> {
            if(element.getIsPrimary().equals(true)){
                menuDTO.setIsPrimary(false);
                menuRepository.save(element);
            }
        });
        Menu menu = modelMapper.map(menuDTO, Menu.class);
        menu.setRestaurant(restaurant);
        menu.setMenuItemsList(new ArrayList<>());

        Menu savedMenu = menuRepository.save(menu);
        return modelMapper.map(savedMenu, MenuDTO.class);
    }

    @Override
    public MenuDTO updateMenuByRestaurant(Long restaurantId, Long menuId, MenuDTO menuDTO) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "menuId", menuId));

        Menu newMenu = modelMapper.map(menuDTO, Menu.class);
        newMenu.setRestaurant(restaurant);
        newMenu.setId(menu.getId());
        List<MenuItemDTO> menuItemDTOS = menu.getMenuItemsList().stream().map(item -> modelMapper.map(item, MenuItemDTO.class)).toList();
        newMenu.setMenuItemsList(menu.getMenuItemsList());
        menuRepository.save(newMenu);
        MenuDTO newMenuDTO = modelMapper.map(newMenu, MenuDTO.class);
        newMenuDTO.setMenuItemsList(menuItemDTOS);
        return newMenuDTO;
    }

    @Override
    public MenuDTO deleteMenuByRestaurant(Long restaurantId, Long menuId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "menuId", menuId));

        restaurant.getMenus().remove(menu);
        menuRepository.deleteById(menuId);
        menuRepository.flush();
        restaurantRepository.save(restaurant);
        return modelMapper.map(menu, MenuDTO.class);
    }

    @Override
    public MenuDTO setPrimaryMenu(Long restaurantId, Long menuId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", "restaurantId", restaurantId));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu", "menuId", menuId));
        menu.setRestaurant(restaurant);
        menu.setIsPrimary(true);
        menuRepository.save(menu);
        List<Menu> menuList = restaurant.getMenus().stream().toList();
        //stream each menu except the one that was passed into the DTO to be disabled
        menuList.stream().forEach(item -> {
            if(!item.getId().equals(menu.getId()))
            {
                item.setIsPrimary(false);
                menuRepository.save(item);
            }
        });
        return modelMapper.map(menu, MenuDTO.class);
    }
}
