/*
written by Ty Bennett
 */

package com.carolinawings.webapp.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="users")
@Getter
@Setter
public class User {
	//Identifying information
    @Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(nullable = false)
	//UUID of user
	private UUID id;
    @Column(nullable = false)
	//name of user
	private String name;
	@Column(nullable = false)
	//email of users
	private String email;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String phoneNumber;
	//Are they a member of mailing list
	@Column(nullable = false)
	private Boolean newsletterMember;
	//keep track of how old account is
	@Column(nullable = false)
	private LocalDate dateJoined;
	//Keep track of orders with a list of orders
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<CustomerOrder> orderHistory = new ArrayList<CustomerOrder>();
}
