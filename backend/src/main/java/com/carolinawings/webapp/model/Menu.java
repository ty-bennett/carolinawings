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

    @ManyToMany(mappedBy = "menus")
    private Set<Restaurant> restaurants = new HashSet<>();

    public Menu(String name, String description, List<MenuItem> menuItemsList, Set<Restaurant> restaurants) {
        this.name = name;
        this.description = description;
        this.menuItemsList = menuItemsList;
        this.restaurants = restaurants;
    }

    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description=" + description + '\'' +
                ", menuItemsList=" + menuItemsList +
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
