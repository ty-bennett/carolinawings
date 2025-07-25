package com.carolinawings.webapp.model;

import jakarta.persistence.*;

@Entity
public class MenuItemOption {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private String name; // sauce/dressing name
    @Enumerated(EnumType.STRING)
    private String type; // type (sauce or dressing)\

    @ManyToOne
    private MenuItem menuItem;
}
