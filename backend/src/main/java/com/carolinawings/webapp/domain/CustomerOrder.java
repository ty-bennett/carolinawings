/*
 * Ty Bennett
 */

package com.carolinawings.webapp.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity //will  CREATE TABLE ORDER () in SQL based on Class name
public class CustomerOrder {
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private Long restaurantAssignedTo;
	private LocalTime pickupTime;
	private LocalDateTime orderDateTime;
	private BigDecimal orderAmount;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getRestaurantAssignedTo() {
		return restaurantAssignedTo;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public void setRestaurantAssignedTo(Long restaurantAssignedTo) {
		this.restaurantAssignedTo = restaurantAssignedTo;
	}
	public LocalTime getPickupTime() {
		return pickupTime;
	}
	public void setPickupTime(LocalTime pickupTime) {
		this.pickupTime = pickupTime;
	}
	public LocalDateTime getOrderDateTime() {
		return orderDateTime;
	}
	public void setOrderDateTime(LocalDateTime orderDateTime) {
		this.orderDateTime = orderDateTime;
	}
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
}
