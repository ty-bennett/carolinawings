/*
Ty Bennett
*/
package com.carolinawings.webapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    @ManyToMany(mappedBy = "restaurants")
    private Set<User> restaurantAdmin;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Menu> menus;

    public Restaurant(String name, String address, Set<User> restaurantAdmin, Set<Menu> menus) {
        this.name = name;
        this.address = address;
        this.restaurantAdmin = restaurantAdmin;
        this.menus = menus;
    }

    public Restaurant() {}
}


