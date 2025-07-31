package com.carolinawings.webapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MenuItemOption {
    @Id
    @GeneratedValue
    private Long id;

    private String name; // sauce/dressing name

    @ManyToOne
    @JoinColumn(name = "option_group_id")
    private OptionGroup group;

    public MenuItemOption(String mild, String sauce) {
    }
}
