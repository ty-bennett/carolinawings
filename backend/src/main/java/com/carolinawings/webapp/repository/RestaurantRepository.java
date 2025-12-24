/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByName(String name);
    Optional<Restaurant> findById(Long id);
    @Query("SELECT r FROM Restaurant r JOIN r.restaurantAdmin u WHERE u.id = :userId")
    Page<Restaurant> findAllRestaurantsByUserId (@Param("userId") UUID userId, Pageable pageable);
}
