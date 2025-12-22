package com.carolinawings.webapp.controller_test;

import com.carolinawings.webapp.controller.AdminMenuController;
import com.carolinawings.webapp.dto.MenuDTO;
import com.carolinawings.webapp.service.MenuItemServiceImplementation;
import com.carolinawings.webapp.service.MenuServiceImplementation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = AdminMenuController.class)
@AutoConfigureMockMvc
public class AdminMenuControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuServiceImplementation menuService;

    @MockitoBean
    private MenuItemServiceImplementation menuItemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanGetMenuById() throws Exception {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(1L);
        menuDTO.setName("test menu");

        when(menuService.getMenuById(1L))
                .thenReturn(Optional.of(menuDTO));

        mockMvc.perform(get("/api/admin/menus/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("test menu"));

    }

 }