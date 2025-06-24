/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    Restaurant findByName(String name);
}
