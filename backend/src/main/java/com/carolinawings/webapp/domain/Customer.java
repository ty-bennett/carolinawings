package com.carolinawings.webapp.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="customers")
public class Customer {
	//Identifying information
    @Id @GeneratedValue(strategy = GenerationType.UUID)
	@Column(nullable = false)
	@Getter
	@Setter
	//UUID of user
	private UUID id;
    @Column(nullable = false)
	@Getter
	@Setter
	//name of user
	private String name;
	@Column(nullable = false)
	@Getter
	@Setter
	//email of users
	private String email;
	@Column(nullable = false)
	@Getter
	@Setter
	private String password;
	@Column(nullable = false)
	@Getter
	@Setter
	private String phoneNumber;
	//Are they a member of mailing list
	@Column(nullable = false)
	@Getter
	@Setter
	private Boolean newsletterMember;
	//keep track of how old account is
	@Column(nullable = false)
	@Getter
	@Setter
	private LocalDate dateJoined;
	//Keep track of orders with a list of orders
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	@Getter
	@Setter
	private List<CustomerOrder> orderHistory = new ArrayList<CustomerOrder>();
}
