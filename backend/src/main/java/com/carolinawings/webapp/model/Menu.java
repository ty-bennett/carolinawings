/*
Ty Bennett
*/

package com.carolinawings.webapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "menus")

public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItem> menuItemsList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    private Boolean isPrimary;

    public Menu(String name, String description, List<MenuItem> menuItemsList, Restaurant restaurant, Boolean isPrimary) {
        this.name = name;
        this.description = description;
        this.menuItemsList = menuItemsList;
        this.restaurant = restaurant;
        this.isPrimary = isPrimary;
    }

    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description=" + description + '\'' +
                ", menuItemsList=" + menuItemsList + '\'' +
                ", isPrimary=" + isPrimary +
                '}';
    }

    public boolean equals(Menu m)
    {
        return m != null &&
            this.getId().equals(m.getId()) &&
            this.getName().equals(m.getName()) &&
            this.getMenuItemsList().equals(m.getMenuItemsList());
    }
}
