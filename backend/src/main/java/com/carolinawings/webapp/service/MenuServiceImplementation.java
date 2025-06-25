/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.MenuDTO;
import com.carolinawings.webapp.dto.MenuResponse;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Menu;
import com.carolinawings.webapp.repository.MenuRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImplementation implements MenuService {

    private final MenuRepository menuRepository;

    @Autowired
    private ModelMapper modelMapper;

    public MenuServiceImplementation(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public MenuResponse getAllMenus() {
        List<Menu> menus = menuRepository.findAll();
        if (menus.isEmpty())
            throw new APIException("No menus present");
        List<MenuDTO> menuDTOS = menus.stream()
                .map(menu -> modelMapper.map(menu, MenuDTO.class))
                .toList();

        return new MenuResponse(menuDTOS);
    }

    @Override
    public MenuResponse getAllMenusPage(Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Menu> menus = menuRepository.findAll(pageDetails);
        List<Menu> menusPageable = menus.getContent();
        if (menusPageable.isEmpty())
            throw new APIException("No menus present");
        List<MenuDTO> menuDTOS = menusPageable.stream()
                .map(menu -> modelMapper.map(menu, MenuDTO.class))
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
        Menu savedMenu = menuRepository.findByName(menu.getName());
        if (savedMenu != null)
            throw new APIException("Menu with the name " + menu.getName() + " already exists");
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
}
