/*
Ty Bennett
 */
package com.carolinawings.webapp.controller_test;

import com.carolinawings.webapp.controller.PublicMenuController;
import com.carolinawings.webapp.dto.MenuDTO;
import com.carolinawings.webapp.service.MenuItemServiceImplementation;
import com.carolinawings.webapp.service.MenuServiceImplementation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = PublicMenuController.class,
        excludeFilters = {
        @ComponentScan.Filter(
                type= FilterType.ASSIGNABLE_TYPE,
                classes = {
                        com.carolinawings.webapp.security.jwt.AuthTokenFilter.class
                }
        )
    }
)
@AutoConfigureMockMvc(addFilters = false)
public class PublicMenuControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuServiceImplementation menuService;

    @MockitoBean
    private MenuItemServiceImplementation menuItemService;

    @Test
    void publicCanGetMenuById() throws Exception {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(1L);
        menuDTO.setName("test menu");

        when(menuService.getMenuById(1L))
                .thenReturn(Optional.of(menuDTO));

        mockMvc.perform(get("/api/menus/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test menu"));

    }

    @Test
    void menuNotFound() throws Exception {
        when(menuService.getMenuById(99L))
                .thenReturn(Optional.empty());
        mockMvc.perform(get("/api/menus/99"))
                .andExpect(status().isNotFound());
    }

}
