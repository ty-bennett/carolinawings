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

    private String name; // sauce/dressing/other name

    @ManyToOne
    @JoinColumn(name = "option_group_id")
    private OptionGroup group = new OptionGroup();

    public MenuItemOption(String name) {
        this.name = name;
    }
}
