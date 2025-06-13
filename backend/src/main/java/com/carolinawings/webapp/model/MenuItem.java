/*
Written by Ty Bennett
 */
package com.carolinawings.webapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String imageURL;
    private BigDecimal price;
    private String category;
    private boolean enabled;

    public MenuItem(String name, String description, String imageURL, BigDecimal price, String category, boolean enabled) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.price = price;
        this.category = category;
        this.enabled = enabled;
    }
    public MenuItem() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @java.lang.Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", enabled=" + enabled +
                '}';
    }

    public boolean equals(MenuItem m)
    {
       return m != null &&
           this.id.equals(m.getId()) &&
           this.name.equals(m.getName()) &&
           this.description.equals(m.getDescription()) &&
           this.imageURL.equals(m.getImageURL()) &&
           this.price.equals.(m.getPrice()) &&
           this.category.equals(m.getCategory()) &&
           this.enabled.equals(m.isEnabled());
    }
}