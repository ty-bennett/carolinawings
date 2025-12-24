package com.carolinawings.webapp.security;

import com.carolinawings.webapp.enums.RoleName;
import com.carolinawings.webapp.model.Order;
import com.carolinawings.webapp.model.Restaurant;
import com.carolinawings.webapp.model.User;
import com.carolinawings.webapp.repository.OrderRepository;
import com.carolinawings.webapp.repository.RestaurantRepository;
import com.carolinawings.webapp.repository.UserRepository;
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

    public SecurityService(AuthUtil authUtil,
                           AuthenticationManager authenticationManager,
                           DaoAuthenticationProvider authenticationProvider,
                           RestaurantRepository restaurantRepository,
                           OrderRepository orderRepository) {
        this.authUtil = authUtil;
        this.authenticationManager = authenticationManager;
        this.authenticationProvider = authenticationProvider;
        this.restaurantRepository = restaurantRepository;
        this.orderRepository = orderRepository;
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

    public boolean canManageUser(Long userId) {
        User currentUser = authUtil.loggedInUser();
        if (currentUser == null) {
            return false;
        }
        return (currentUser.getRoles().stream().anyMatch(role -> role.getName() == RoleName.ADMIN));
    }

    //TODO: finish
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
        } else if(currentUser.equals(order.getUser())) {
            return true;
        }
        return false;
    }
    //TODO: implement
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

        // not right
        return false;
    }
}
