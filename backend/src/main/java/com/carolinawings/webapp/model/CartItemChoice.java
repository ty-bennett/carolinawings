package com.carolinawings.webapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemChoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private CartItem cartItem;

    @ManyToOne
    private MenuItemOption menuItemOption;

    @Column(nullable = false)
    private String choiceType;
}
