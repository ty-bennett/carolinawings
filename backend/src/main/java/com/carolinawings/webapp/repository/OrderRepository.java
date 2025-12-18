/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Order findOrderById(UUID id);

    Order getOrderById(UUID id);
    // Page<Order> findOrdersByRestaurantAssignedTo(Long restaurantAssignedTo, Pageable pageable);
}
