/*
Ty Bennett
*/
package com.carolinawings.webapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    @ManyToMany(mappedBy = "restaurants")
    private Set<User> restaurantAdmin;
    @ManyToMany
    @JoinTable(
            name = "restaurant_menus",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id")
    )
    private Set<Menu> menus;

    public Restaurant(String name, String address, Set<User> restaurantAdmin, Set<Menu> menus) {
        this.name = name;
        this.address = address;
        this.restaurantAdmin = restaurantAdmin;
        this.menus = menus;
    }

    public Restaurant() {}

    public boolean equals(Restaurant r)
    {
        return r != null &&
            this.getId().equals(r.getId()) &&
            this.getName().equals(r.getName()) &&
            this.getAddress().equals(r.getAddress()) &&
            this.getRestaurantAdmin().equals(r.getRestaurantAdmin());
    }

    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", restaurantAdmin=" + restaurantAdmin +
                '}';
    }
}


