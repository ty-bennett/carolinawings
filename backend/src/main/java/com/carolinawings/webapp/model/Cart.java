package com.carolinawings.webapp.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private LocalDateTime orderCreated;
    private LocalDateTime pickupTime;
    @OneToOne
    private User userAssignedTo;
}
