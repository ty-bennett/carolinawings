package com.carolinawings.webapp;

import com.carolinawings.webapp.domain.MenuItem;
import com.carolinawings.webapp.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

    @Autowired
    private MenuItemRepository menuRepository;

    @GetMapping
    public List<MenuItem> getAll() {
        return menuRepository.findAll();
    }
}
