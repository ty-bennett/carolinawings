/*
Ty Bennett
*/
package com.carolinawings.webapp.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    @ManyToMany
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissionsList;

    public Role() {}

    public Role(Long id, String name, String description, Set<Permission> permissionsList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissionsList = permissionsList;
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

    public Set<Permission> getPermissionsList() {
        return permissionsList;
    }

    public void setPermissionsList(Set<Permission> permissionsList) {
        this.permissionsList = permissionsList;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", permissionsList=" + permissionsList +
                '}';
    }

    public boolean equals(Role r)
    {
        return r != null &&
            this.getId().equals(r.getId()) &&
            this.getName().equals(r.getName()) &&
            this.getDescription().equals(r.getDescription()) &&
            this.getPermissionsList().equals(r.getPermissionsList());
    }
}
