/*
 Written by Ty Bennett
 */

package com.carolinawings.webapp.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.carolinawings.webapp.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity //will  CREATE TABLE ORDER () in SQL based on Class name
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
	//a unique identifier given to each order upon creation
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	//a restaurant that the order is assigned to
	@ManyToOne
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;
	//a time that is set by user to choose when to pickup order
	private OffsetDateTime pickupTime;
	//time of order creation
	@Column(nullable = false)
	private OffsetDateTime createdAt;
	// last updated at
	@Column(nullable = false)
	private OffsetDateTime updatedAt;
	//price of order
	@Column(precision = 10, scale = 2)
	private BigDecimal totalPrice;
	// tax
	@Column(precision = 10, scale = 2)
	private BigDecimal totalTax;
	// subtotal (before tax)
	@Column(precision = 10, scale = 2)
	private BigDecimal subtotal;
	//User assigned order to
	@ManyToOne
	private User user;
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> items = new ArrayList<>();
	// enum for status of order
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;

	private String customerNotes;

	@PrePersist
	public void prePersist() {
		if(this.status == null)
			this.status = OrderStatus.PENDING;
		this.createdAt = OffsetDateTime.now();
		this.updatedAt = OffsetDateTime.now();
		if(this.pickupTime == null) {
			this.pickupTime = OffsetDateTime.now().plusMinutes(15);
		}
	}
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = OffsetDateTime.now();
	}
}
