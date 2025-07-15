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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        Page<Menu> menus = menuRepository.findByRestaurantsId(restaurantId, pageDetails);

        List<Menu> menusPageable = menus.getContent();
        if (menusPageable.isEmpty())
            throw new APIException("No menus present");
        List<MenuItemDTO> menuItemDTOS = menuItemRepository.findAll().stream().map(item -> modelMapper.map(item, MenuItemDTO.class)).collect(Collectors.toList());
        List<MenuDTO> menuDTOS = menusPageable.stream()
                .map(menu -> {
                    MenuDTO dto = new MenuDTO();
                    dto.setId(menu.getId());
                    dto.setName(menu.getName());
                    dto.setDescription(menu.getDescription());
                    dto.setMenuItemsList(menuItemDTOS);

                    if (!menu.getRestaurants().isEmpty()) {
                        dto.setRestaurantId(menu.getRestaurants().iterator().next().getId());
                    }

                    return dto;
                })
                .toList();
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
        Set<Menu> menusToSearch = restaurant.getMenus() == null ? new HashSet<>() : restaurant.getMenus();
        Menu menu = menusToSearch.stream().filter(menuItem -> menuItem.getId().equals(menuId)).findFirst().get();
        return modelMapper.map(menu, MenuDTO.class);
    }
}
