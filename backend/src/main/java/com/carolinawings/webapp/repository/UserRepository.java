/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.Restaurant;
import com.carolinawings.webapp.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername(String email);
    Optional<User> findByUsername(String username);
    @Query("SELECT r FROM Restaurant r JOIN r.restaurantAdmin u WHERE u.id = :userId")
    Set<Restaurant> findAllRestaurantsByUserId(@Param("userId") UUID userId);
}
