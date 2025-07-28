package com.carolinawings.webapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemOption {
    @Id
    @GeneratedValue
    private Long id;

    private String name; // sauce/dressing name
    private String type; // type (sauce or dressing)\

    @ManyToOne
    private MenuItem menuItem;
}
