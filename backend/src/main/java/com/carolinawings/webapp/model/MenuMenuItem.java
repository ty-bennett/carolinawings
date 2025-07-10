package com.carolinawings.webapp.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "menu_menu_item",
        uniqueConstraints =
        {
        @UniqueConstraint(columnNames = {"menu_id", "menu_item_id"})
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class MenuMenuItem{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many mappings can belong to one menu
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    // Many mappings can belong to one menu item
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    // Contextual field for this pairing
    private String status;


}