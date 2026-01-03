/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.dto.OrderDTO;
import com.carolinawings.webapp.enums.OrderStatus;
import com.carolinawings.webapp.model.Order;
import com.carolinawings.webapp.model.Restaurant;
import com.carolinawings.webapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findOrderById(UUID id);
    Page<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, Pageable pageable);
    Page<Order> findByRestaurantId(Long restaurantId, Pageable pageable);

    Page<Order> findByUser(User user, Pageable pageable);
}
