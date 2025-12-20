/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.enums.OrderStatus;
import com.carolinawings.webapp.model.Order;
import com.carolinawings.webapp.model.Restaurant;
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

    Page<Order> findOrdersByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, Pageable pageable);
    Page<Order> findOrdersByRestaurantId(Long restaurantId, Pageable pageable);
}
