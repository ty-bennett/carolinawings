package com.carolinawings.webapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "menu_item_option_groups",
        uniqueConstraints = @UniqueConstraint(columnNames = {
                "menu_item_id", "option_group_id"
        })
)
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
    private int minChoices = 1;
    private int maxChoices;
    private boolean overrideMaxChoices;

    @ManyToOne
    private MenuItem menuItem;

    @ManyToOne
    private OptionGroup optionGroup;
}


