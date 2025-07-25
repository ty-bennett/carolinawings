package com.carolinawings.webapp.model;

import jakarta.persistence.*;

@Entity
public class MenuItemOptionRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private String optionType;

    @ManyToOne
    private MenuItem menuItem;

    private Integer minQuantity;
    private Integer maxQuantity;
    private Integer maxChoices;
}
