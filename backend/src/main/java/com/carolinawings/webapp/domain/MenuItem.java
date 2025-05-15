/*
Written by Ty Bennett
 */
package com.carolinawings.webapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
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
    private String category;
}
