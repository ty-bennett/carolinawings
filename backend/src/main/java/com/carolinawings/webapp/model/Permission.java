/*
Ty Bennett
*/

package com.carolinawings.webapp.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    @ManyToMany(mappedBy = "permissionsList")
    private Set<Role> roles;

    public Permission() {}

    public Permission(Long id, String name, String description, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Role> getRolesMember() {
        return roles;
    }

    public void setRolesMember(Set<Role> roles) {
        this.roles = roles;
    }



    public boolean equals(Permission p)
    {
        return p != null &&
            this.getId().equals(p.getId()) &&
            this.getName().equals(p.getName()) &&
            this.getDescription().equals(p.getDescription()) &&
            this.getRolesMember().equals(p.getRolesMember());
    }
}
