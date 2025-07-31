package com.carolinawings.webapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemOptionGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionType; // e.g., "sauce", "dressing"
    private boolean required;
    private int minChoices;
    private int maxChoices;

    @ManyToOne
    private MenuItem menuItem;

    @ManyToOne
    private OptionGroup optionGroup;
}


