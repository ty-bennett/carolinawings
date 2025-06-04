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
    @OneToOne
    private User userAssignedTo;

    public Cart() {
        this.orderCreated = LocalDateTime.now();
        this.pickupTime = null;
        this.userAssignedTo = null;
    }

    public Long getId() {
        return this.id;
    } 

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOrderCreated() {
        return this.orderCreated;
    }

    public void setOrderCreated(LocalDateTime orderCreated) {
        this.orderCreated = orderCreated;
    }

    public LocalDateTime getPickupTime() {
        return this.pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public User getUserAssignedTo() {
        return this.userAssignedTo;
    }

    public void setUserAssignedTo(User userAssignedTo) {
        this.userAssignedTo = userAssignedTo;
    }

    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", orderCreated=" + orderCreated +
                ", pickupTime=" + pickupTime +
                ", userAssignedTo=" + (userAssignedTo != null ? userAssignedTo.getName() : "null") +
                '}';
    }
}
