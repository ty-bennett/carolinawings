package com.carolinawings.webapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private List<MenuItem> cart = new ArrayList<>();
    private LocalDateTime orderCreated;
    private LocalDateTime pickupTime;
    private User userAssignedTo;
}
