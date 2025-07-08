/*
Written by Ty Bennett
 */

package com.carolinawings.webapp.model;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class User {
    //Identifying information
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    //UUID of user
    private UUID id;
    //name of user
    private String name;
    //email of users
    @Email(message = "Email should be valid")
    @Size(max=50)
    private String email;
    @Size(min=8, max=100, message = "Password must be at least 8 characters long")
    private String password;
    private String phoneNumber;
    //Are they a member of mailing list
    private Boolean newsletterMember;
    //keep track of how old account is
    private LocalDate dateJoined;
    //set status of User
    private Boolean enabled;
    @OneToMany(mappedBy = "user")
    private List<Order> orderHistoryList;
    @Enumerated(EnumType.STRING)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", newsletterMember=" + newsletterMember +
                ", dateJoined=" + dateJoined +
                ", enabled=" + enabled +
                ", ordersList= " + orderHistoryList +
                '}';
    }

    public boolean equals(User u) {
        return u != null &&
                this.id.equals(u.id) &&
                this.name.equals(u.name) &&
                this.email.equals(u.email) &&
                this.password.equals(u.password) &&
                this.phoneNumber.equals(u.phoneNumber) &&
                this.newsletterMember.equals(u.newsletterMember) &&
                this.dateJoined.equals(u.dateJoined) &&
                this.enabled.equals(u.enabled) &&
                this.orderHistoryList.equals(u.orderHistoryList);
        }
    }