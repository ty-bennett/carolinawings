/*
Ty Bennett
*/

package com.carolinawings.webapp.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "menus")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany
    private List<MenuItem> menuItemsList;

    public Menu() {}

    public Menu(Long id, String name, List<MenuItem> menuItemsList) {
        this.id = id;
        this.name = name;
        this.menuItemsList = menuItemsList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuItem> getMenuItemsList() {
        return menuItemsList;
    }

    public void setMenuItemsList(List<MenuItem> menuItemsList) {
        this.menuItemsList = menuItemsList;
    }

    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
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
