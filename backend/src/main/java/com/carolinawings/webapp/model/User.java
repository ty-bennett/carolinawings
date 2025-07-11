/*
Written by Ty Bennett
 */

package com.carolinawings.webapp.model;

import java.time.LocalDate;
import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor

public class User {
    //Identifying information
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    //UUID of user
    @Column(name = "id")
    private UUID id;
    //name of user
    @Column(name = "name")
    @Size(min = 2, max = 40, message = "Name must be at least 2 characters long")
    private String name;
    //email of users
    @Email(message = "Email should be valid")
    @Size(max=50, min = 7, message = "Email must be a valid email")
    private String username;
    @Size(min=8, max=120, message = "Password must be at least 8 characters long")
    @Column(name = "password")
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "newsletter_member")
    //Are they a member of mailing list
    private Boolean newsletterMember;
    @Column(name = "date_joined")
    //keep track of how old account is
    private LocalDate dateJoined;
    //set status of User
    @OneToMany(mappedBy = "user")
    private List<Order> orderHistoryList;
    @Enumerated(EnumType.STRING)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User(@NotBlank @Size(min = 3, max = 40) @Email String username, @NotBlank @Size(min = 8, max=50) String password, String encode, String phoneNumber, boolean newsletterMember, String name) {
        this.name = name;
        this.username = username;
        this.password = encode;
        this.newsletterMember = newsletterMember;
        this.dateJoined = LocalDate.now();
        this.roles = new HashSet<>();
        this.phoneNumber = phoneNumber;
        this.orderHistoryList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", newsletterMember=" + newsletterMember +
                ", dateJoined=" + dateJoined +
                ", ordersList= " + orderHistoryList +
                '}';
    }

    public boolean equals(User u) {
        return u != null &&
                this.id.equals(u.id) &&
                this.name.equals(u.name) &&
                this.username.equals(u.username) &&
                this.password.equals(u.password) &&
                this.phoneNumber.equals(u.phoneNumber) &&
                this.newsletterMember.equals(u.newsletterMember) &&
                this.dateJoined.equals(u.dateJoined) &&
                this.orderHistoryList.equals(u.orderHistoryList);
        }
    }