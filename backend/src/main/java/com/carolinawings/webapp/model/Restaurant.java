package com.carolinawings.webapp.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table
public class Restaurant {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String address;
    @OneToOne
    private Manager restaurantAdmin;

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

    public Manager getRestaurantAdmin() {
        return restaurantAdmin;
    }

    public void setRestaurantAdmin(Manager restaurantAdmin) {
        this.restaurantAdmin = restaurantAdmin;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Restaurant that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(restaurantAdmin, that.restaurantAdmin);
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


