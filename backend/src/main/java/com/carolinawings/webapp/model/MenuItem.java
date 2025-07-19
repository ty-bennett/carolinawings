/*
Written by Ty Bennett
 */
package com.carolinawings.webapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
@NoArgsConstructor

public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String imageURL;
    private BigDecimal price;
    private String category;
    private Boolean enabled;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CartItem> menuItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = true)
    private Menu menu;

    public MenuItem(String name, String description, String imageURL, BigDecimal price, String category, Boolean enabled) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.price = price;
        this.category = category;
        this.enabled = enabled;
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

