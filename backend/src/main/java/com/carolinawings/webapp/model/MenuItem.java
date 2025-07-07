/*
Written by Ty Bennett
 */
package com.carolinawings.webapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String imageURL;
    private BigDecimal price;
    private String category;
    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    private List<MenuMenuItem> menus = new ArrayList<>();

    public MenuItem(String name, String description, String imageURL, BigDecimal price, String category) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.price = price;
        this.category = category;
    }
    public MenuItem() {}

    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                '}';
    }

    public boolean equals(MenuItem m)
    {
       return m != null &&
           this.id.equals(m.getId()) &&
           this.name.equals(m.getName()) &&
           this.description.equals(m.getDescription()) &&
           this.imageURL.equals(m.getImageURL()) &&
           this.price.equals(m.getPrice()) &&
           this.category.equals(m.getCategory());
    }
}

