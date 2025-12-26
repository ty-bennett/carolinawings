package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.RestaurantHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantHoursRepository extends JpaRepository<RestaurantHours, Long> {
    List<RestaurantHours> findByRestaurantIdOrderByDayOfWeek(Long restaurantId);
    Optional<RestaurantHours> findByRestaurantIdAndDayOfWeek(Long restaurantId, DayOfWeek dayOfWeek);
    void deleteByRestaurantId(Long restaurantId);
}