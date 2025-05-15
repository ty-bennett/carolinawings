/*
 * Ty Bennett
 */

package com.carolinawings.webapp.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity //will  CREATE TABLE ORDER () in SQL based on Class name
@Table(name = "customer_order")
public class CustomerOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(nullable = false)
	private UUID id;
	@Column(nullable = false)
	private Long restaurantAssignedTo;
	@Column(nullable = false)
	private LocalTime pickupTime;
	@Column(nullable = false)
	private LocalDateTime orderDateTime;
	@Column(nullable = false)
	private BigDecimal orderAmount;
	@ManyToOne(optional = false)
	@JoinColumn(name = "customer_id")
	private Customer customer;
}
