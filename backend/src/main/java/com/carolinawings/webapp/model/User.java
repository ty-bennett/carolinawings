/*
Written by Ty Bennett
 */

package com.carolinawings.webapp.model;

import java.time.LocalDate;
import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name="users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        })
@Getter
@Setter
@NoArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "first_name")
    @Size(min = 1, max = 30, message = "First name must be between 1 and 30 characters")
    private String firstName;

    @Column(name = "last_name")
    @Size(min = 1, max = 30, message = "Last name must be between 1 and 30 characters")
    private String lastName;

    @Email(message = "Email should be valid")
    @Size(max=50, min = 7, message = "Email must be a valid email")
    @Column(nullable = false, unique = true)
    private String username;

    @Size(min=8, max=120, message = "Password must be at least 8 characters long")
    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "newsletter_member")
    private Boolean newsletterMember;

    @Column(name = "date_joined")
    private LocalDate dateJoined;

    @OneToMany(mappedBy = "user")
    private List<Order> orderHistoryList;

    @Enumerated(EnumType.STRING)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_restaurant",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id"))
    private Set<Restaurant> restaurants;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Cart> cart;

    public User(@NotBlank @Size(min = 3, max = 40) @Email String username,
                @NotBlank @Size(min = 8, max=50) String password,
                String firstName,
                String lastName,
                String phoneNumber,
                boolean newsletterMember) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.newsletterMember = newsletterMember;
        this.dateJoined = LocalDate.now();
        this.roles = new HashSet<>();
        this.phoneNumber = phoneNumber;
        this.orderHistoryList = new ArrayList<>();
    }

    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", newsletterMember=" + newsletterMember +
                ", dateJoined=" + dateJoined +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}