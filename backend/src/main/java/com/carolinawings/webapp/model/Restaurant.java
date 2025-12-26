package com.carolinawings.webapp.model;

/*
Ty Bennett
*/

import com.carolinawings.webapp.enums.RestaurantStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@ToString
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;

    @Enumerated(EnumType.STRING)
    private RestaurantStatus status = RestaurantStatus.OPEN;

    private boolean acceptingOrders = true;  // Quick toggle for busy mode
    private Integer estimatedPickupMinutes = 15;  // Default pickup time

    @ManyToMany(mappedBy = "restaurants")
    private Set<User> restaurantAdmin;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Menu> menus;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<RestaurantHours> hours;

    public Restaurant(String name, String address, Set<User> restaurantAdmin, Set<Menu> menus) {
        this.name = name;
        this.address = address;
        this.restaurantAdmin = restaurantAdmin;
        this.menus = menus;
    }

    public Restaurant() {}
}
