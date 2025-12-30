package com.carolinawings.webapp.security;

import com.carolinawings.webapp.enums.OrderStatus;
import com.carolinawings.webapp.enums.RoleName;
import com.carolinawings.webapp.model.*;
import com.carolinawings.webapp.repository.*;
import com.carolinawings.webapp.util.AuthUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("securityService")
public class SecurityService {

    private final AuthUtil authUtil;
    private final AuthenticationManager authenticationManager;
    private final DaoAuthenticationProvider authenticationProvider;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;

    public SecurityService(AuthUtil authUtil,
                           AuthenticationManager authenticationManager,
                           DaoAuthenticationProvider authenticationProvider,
                           RestaurantRepository restaurantRepository,
                           OrderRepository orderRepository, MenuItemRepository menuItemRepository, MenuRepository menuRepository) {
        this.authUtil = authUtil;
        this.authenticationManager = authenticationManager;
        this.authenticationProvider = authenticationProvider;
        this.restaurantRepository = restaurantRepository;
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.menuRepository = menuRepository;
    }

    public boolean canManageRestaurant(Long restaurantId) {
        User currentUser = authUtil.loggedInUser();

        if (currentUser == null) {
            return false;
        }

        //ADMIN check
        if (currentUser.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleName.ADMIN)) {
            return true;
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);

        if (restaurant == null) {
            return false;
        }

        return currentUser.getRestaurants().contains(restaurant);
    }

    public boolean canManageMenu(Long menuId) {
        User currentUser = authUtil.loggedInUser();
        if (currentUser == null) {
            return false;
        }
        if (currentUser.getRoles().stream().anyMatch(role -> role.getName() == RoleName.ADMIN)) {
            return true;
        }

        Menu menu = menuRepository.findById(menuId).orElse(null);
        if (menu == null) {
            return false;
        }

        Restaurant restaurant = menu.getRestaurant();
        if (restaurant == null) {
            return false;
        }

        return currentUser.getRestaurants().contains(restaurant);
    }

    public boolean canManageUser(Long userId) {
        User currentUser = authUtil.loggedInUser();
        if (currentUser == null) {
            return false;
        }
        return (currentUser.getRoles().stream().anyMatch(role -> role.getName() == RoleName.ADMIN));
    }

    public boolean canManageOrder(UUID orderId) {
        User currentUser = authUtil.loggedInUser();
        if (currentUser == null) {
            return false;
        }
        if (currentUser.getRoles().stream().anyMatch(role -> role.getName() == RoleName.ADMIN)) {
            return true;
        }
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return false;
        }
        Restaurant restaurant = order.getRestaurant();
        if (currentUser.getRoles().stream().anyMatch(role -> role.getName() == RoleName.MANAGER || role.getName() == RoleName.RESTAURANT_ADMIN)) {
            return currentUser.getRestaurants().contains(restaurant);
        }
        return false;
    }

    public boolean canCancelOrder(UUID orderId) {
        User currentUser = authUtil.loggedInUser();
        if (currentUser == null) {
            return false;
        }
        if (currentUser.getRoles().stream().anyMatch(role -> role.getName() == RoleName.ADMIN)) {
            return true;
        }
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return false;
        }

        Restaurant restaurant = order.getRestaurant();
        if (currentUser.getRoles().stream().anyMatch
                (role -> role.getName() == RoleName.RESTAURANT_ADMIN ||  role.getName() == RoleName.MANAGER)) {
            return currentUser.getRestaurants().contains(restaurant);
        }

        OrderStatus status = order.getStatus();
        if (status == OrderStatus.PENDING) {
            if (order.getUser().equals(currentUser)) {
                return true;
            }
        }
        return false;
    }

    public boolean canViewOrder(UUID orderId) {
        User currentUser = authUtil.loggedInUser();
        if (currentUser == null) {
            return false;
        }
        if (currentUser.getRoles().stream().anyMatch(role -> role.getName() == RoleName.ADMIN)) {
            return true;
        }
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return false;
        }
        Restaurant restaurant = order.getRestaurant();
        if(currentUser.getRoles().stream().anyMatch(
                role -> role.getName() == RoleName.MANAGER || role.getName() == RoleName.RESTAURANT_ADMIN)) {
            return currentUser.getRestaurants().contains(restaurant);
        }

        if(order.getUser().equals(currentUser)) {
            return true;
        }
        return false;
    }

    public boolean canManageMenuItem(Long menuItemId) {
        User currentUser = authUtil.loggedInUser();
        if (currentUser == null) {
            return false;
        }

        if (currentUser.getRoles().stream().anyMatch(role -> role.getName() == RoleName.ADMIN)) {
            return true;
        }

        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElse(null);
        if(menuItem == null) {
            return false;
        }

        Menu menu = menuItem.getMenu();
        if(menu == null) {
            return false;
        }

        Restaurant restaurant = menu.getRestaurant();
        if(restaurant == null) {
            return false;
        }

        if(currentUser.getRoles().stream().anyMatch(
                role -> role.getName() == RoleName.ADMIN || role.getName() == RoleName.RESTAURANT_ADMIN || role.getName() == RoleName.MANAGER)) {
            boolean contains = currentUser.getRestaurants().contains(restaurant);
            return contains;
        }

        return false;
    }
}
