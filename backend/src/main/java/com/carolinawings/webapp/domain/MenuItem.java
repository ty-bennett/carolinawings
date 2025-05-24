/*
Written by Ty Bennett
 */
package com.carolinawings.webapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;
    @Column(nullable = false)
    private String name;
    private String description;
    private String imageURL;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private String category;

    public MenuItem(String name, String description, String imageURL, BigDecimal price, String category) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.price = price;
        this.category = category;
    }
    public MenuItem() {}
}