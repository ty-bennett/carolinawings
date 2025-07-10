package com.carolinawings.webapp.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
public class Cart {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private LocalDateTime orderCreated;
    private LocalDateTime pickupTime;

    public Cart() {
        this.orderCreated = LocalDateTime.now();
        this.pickupTime = LocalDateTime.now().plusMinutes(30);
    }
}


 //this could be handled clientside, and porbably will be but I will get to it