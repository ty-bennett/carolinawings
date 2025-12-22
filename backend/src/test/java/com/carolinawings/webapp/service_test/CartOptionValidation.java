package com.carolinawings.webapp.service_test;

import com.carolinawings.webapp.dto.AddCartItemDTO;
import com.carolinawings.webapp.dto.SelectedOptionGroupDTO;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.model.*;
import com.carolinawings.webapp.repository.*;
import com.carolinawings.webapp.service.CartService;
import com.carolinawings.webapp.util.AuthUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@WithMockUser(username = "test@example.com")
@ActiveProfiles("test")
@Transactional
public class CartOptionValidation {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private MenuItemOptionGroupRepository menuItemOptionGroupRepository;
    @Autowired
    private MenuItemOptionRepository menuItemOptionRepository;
    @Autowired
    private OptionGroupRepository optionGroupRepository;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testWithValidOptions_shouldPass() {
        User user = userRepository.findByUsername("test@example.com")
                .orElseGet(() -> {
                    User u = new User();
                    u.setUsername("test@example.com");
                    u.setPassword(passwordEncoder.encode("password123"));
                    u.setName("Test User");
                    u.setPhoneNumber("8035551234");
                    u.setDateJoined(LocalDate.now());
                    return userRepository.save(u);
                });

        authUtil.setTestUser(user);

        // --- OptionGroup (SAVE FIRST) ---
        OptionGroup sauces = new OptionGroup();
        sauces.setName("Sauces");
        sauces = optionGroupRepository.save(sauces);

        // --- MenuItem ---
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Wings");
        menuItem.setPrice(new BigDecimal("10.00"));
        menuItem.setEnabled(true);
        menuItem = menuItemRepository.save(menuItem);

        // --- MenuItemOptionGroup ---
        MenuItemOptionGroup miog = new MenuItemOptionGroup();
        miog.setMenuItem(menuItem);
        miog.setOptionGroup(sauces);
        miog.setRequired(true);
        miog.setMinChoices(1);
        miog.setMaxChoices(2);
        miog = menuItemOptionGroupRepository.save(miog);

        // --- MenuItemOptions ---
        MenuItemOption ranch = new MenuItemOption();
        ranch.setName("Ranch");
        ranch.setPrice(BigDecimal.ZERO);
        ranch.setOptionGroup(sauces);
        ranch = menuItemOptionRepository.save(ranch);

        MenuItemOption mild = new MenuItemOption();
        mild.setName("Mild");
        mild.setPrice(BigDecimal.ZERO);
        mild.setOptionGroup(sauces);
        mild = menuItemOptionRepository.save(mild);

        // --- Request ---
        AddCartItemDTO dto = new AddCartItemDTO();
        dto.setMenuItemId(menuItem.getId());
        dto.setQuantity(1);

        SelectedOptionGroupDTO groupDTO =
                new SelectedOptionGroupDTO(
                        miog.getId(),
                        List.of(ranch.getId(), mild.getId())
                );

        dto.setSelectedOptionGroups(List.of(groupDTO));

        // --- Execute ---
        assertDoesNotThrow(() -> cartService.addMenuItemToCart(dto));
    }

    @Test
    @WithMockUser("test@example.com")
    void testMoreOptionsThanMaxOptions_shouldFail() {

        // --- User ---
        User user = userRepository.findByUsername("test@example.com")
                .orElseGet(() -> {
                    User u = new User();
                    u.setUsername("test@example.com");
                    u.setPassword(passwordEncoder.encode("password123"));
                    u.setName("Test User");
                    u.setPhoneNumber("8035551234");
                    u.setDateJoined(LocalDate.now());
                    return userRepository.save(u);
                });
        authUtil.setTestUser(user);

        // --- OptionGroup (SAVE FIRST) ---
        OptionGroup sauces = new OptionGroup();
        sauces.setName("Sauces");
        sauces = optionGroupRepository.save(sauces);

        // --- MenuItem ---
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Wings");
        menuItem.setPrice(new BigDecimal("10.00"));
        menuItem.setEnabled(true);
        menuItem = menuItemRepository.save(menuItem);

        // --- MenuItemOptionGroup ---
        MenuItemOptionGroup miog = new MenuItemOptionGroup();
        miog.setMenuItem(menuItem);
        miog.setOptionGroup(sauces);
        miog.setRequired(true);
        miog.setMinChoices(1);
        miog.setMaxChoices(2);
        miog = menuItemOptionGroupRepository.save(miog);

        // --- MenuItemOptions ---
        MenuItemOption ranch = new MenuItemOption();
        ranch.setName("Ranch");
        ranch.setPrice(BigDecimal.ZERO);
        ranch.setOptionGroup(sauces);
        ranch = menuItemOptionRepository.save(ranch);

        MenuItemOption mild = new MenuItemOption();
        mild.setName("Mild");
        mild.setPrice(BigDecimal.ZERO);
        mild.setOptionGroup(sauces);
        mild = menuItemOptionRepository.save(mild);

        MenuItemOption third_wrong_option = new MenuItemOption();
        third_wrong_option.setName("Third_wrong_option");
        third_wrong_option.setPrice(BigDecimal.valueOf(1L));
        third_wrong_option.setOptionGroup(sauces);
        third_wrong_option = menuItemOptionRepository.save(third_wrong_option);

        // --- Request ---
        AddCartItemDTO dto = new AddCartItemDTO();
        dto.setMenuItemId(menuItem.getId());
        dto.setQuantity(2);

        SelectedOptionGroupDTO groupDTO =
                new SelectedOptionGroupDTO(
                        miog.getId(),
                        List.of(ranch.getId(), mild.getId(), third_wrong_option.getId())
                );

        dto.setSelectedOptionGroups(List.of(groupDTO));

        // --- Execute ---
        assertThrows(APIException.class, () -> cartService.addMenuItemToCart(dto));
    }

    @Test
    @WithMockUser("test@example.com")
    void testMissingRequiredOptionGroup_shouldFail() {


        // --- User ---
        User user = userRepository.findByUsername("test@example.com")
                .orElseGet(() -> {
                    User u = new User();
                    u.setUsername("test@example.com");
                    u.setPassword(passwordEncoder.encode("password123"));
                    u.setName("Test User");
                    u.setPhoneNumber("8035551234");
                    u.setDateJoined(LocalDate.now());
                    return userRepository.save(u);
                });
        authUtil.setTestUser(user);

        // --- OptionGroup (SAVE FIRST) ---
        OptionGroup sauces = new OptionGroup();
        sauces.setName("Sauces");
        sauces = optionGroupRepository.save(sauces);

        // --- MenuItem ---
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Wings");
        menuItem.setPrice(new BigDecimal("10.00"));
        menuItem.setEnabled(true);
        menuItem = menuItemRepository.save(menuItem);

        // --- MenuItemOptionGroup ---
        MenuItemOptionGroup miog = new MenuItemOptionGroup();
        miog.setMenuItem(menuItem);
        miog.setOptionGroup(sauces);
        miog.setRequired(true);
        miog.setMinChoices(1);
        miog.setMaxChoices(2);
        miog = menuItemOptionGroupRepository.save(miog);

        MenuItemOption ranch = new MenuItemOption();
        ranch.setName("Ranch");
        ranch.setPrice(BigDecimal.ZERO);
        ranch.setOptionGroup(sauces);
        ranch.setDefaultSelected(false);
        ranch = menuItemOptionRepository.save(ranch);

        MenuItemOption mild = new MenuItemOption();
        mild.setName("Mild");
        mild.setPrice(BigDecimal.ZERO);
        mild.setOptionGroup(sauces);
        mild.setDefaultSelected(false);
        mild = menuItemOptionRepository.save(mild);

        AddCartItemDTO dto = new AddCartItemDTO();
        dto.setMenuItemId(menuItem.getId());
        dto.setQuantity(1);
        dto.setSelectedOptionGroups(List.of());

        assertThrows(APIException.class, () -> cartService.addMenuItemToCart(dto));

    }

}
