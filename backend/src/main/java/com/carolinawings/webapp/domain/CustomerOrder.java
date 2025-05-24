/*
 * Ty Bennett
 */

package com.carolinawings.webapp.domain;

import java.math.BigDecimal;
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
	//a unique identifier given to each order upon creation
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(nullable = false)
	private UUID id;
	//a restaurant that the order is assigned to
	@Column(nullable = false)
	private Long restaurantAssignedTo;
	//a time that is set by user to choose when to pickup order
	@Column(nullable = false)
	private LocalTime pickupTime;
	//a time to be taken whenever order is set to order
	@Column(nullable = false)
	private LocalDateTime orderDateTime;
	//price of order
	@Column(nullable = false)
	private BigDecimal orderAmount;
	//User assigned order to
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")

	private User user;
}
