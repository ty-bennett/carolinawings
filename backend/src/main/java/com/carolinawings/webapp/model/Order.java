/*
 Written by Ty Bennett
 */

package com.carolinawings.webapp.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

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
	private Long restaurantAssignedTo;
	//a time that is set by user to choose when to pickup order
	private LocalTime pickupTime;
	//a time to be taken whenever order is set to order
	private LocalDateTime orderDateTime;
	//price of order
	private BigDecimal orderAmount;
	//User assigned order to
	@ManyToOne
	private User user;
	@ManyToMany
	@JoinTable(
			name = "order_menu_items",
			joinColumns = @JoinColumn(name = "order_id"),
			inverseJoinColumns = @JoinColumn(name = "menu_item_id")
	)
	private List<MenuItem> listOfMenuItems;
}
