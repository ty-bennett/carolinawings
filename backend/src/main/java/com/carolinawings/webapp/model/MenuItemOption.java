package com.carolinawings.webapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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
    private BigDecimal price; //price of sauce if not
    @ManyToOne
    private OptionGroup optionGroup;

    @Column(nullable = false)
    private boolean defaultSelected;

}
