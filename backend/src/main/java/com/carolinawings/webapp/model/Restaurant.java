/*
Ty Bennett
*/
package com.carolinawings.webapp.model;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String address;
    @OneToMany
    private Set<Manager> restaurantAdmin;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Manager> getRestaurantAdmin() {
        return restaurantAdmin;
    }

    public void setRestaurantAdmin(Set<Manager> restaurantAdmin) {
        this.restaurantAdmin = restaurantAdmin;
    }

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


