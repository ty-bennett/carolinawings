package com.carolinawings.webapp.model;

import jakarta.persistence.*;

@Entity
public class CartItemChoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private CartItem cartItem;

    @ManyToOne
    private MenuItemOption menuItemOption;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private String choiceType;
}
