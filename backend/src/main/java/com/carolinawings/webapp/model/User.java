/*
written by Ty Bennett
 */

package com.carolinawings.webapp.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.*;

@Entity
@Table(name="users")
@Getter
@Setter
public class User {
	//Identifying information
    @Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(nullable = false, unique = true)
	//UUID of user
	private UUID id;
    @Column(nullable = false)
	//name of user
	private String name;
	@Column(nullable = false, unique = true)
	@Email
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
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<CustomerOrder> orderHistory = new ArrayList<>();
	private Boolean enabled;

	public User() {}

	public User(String name, String email, String password, String phoneNumber, Boolean newsletterMember, Boolean enabled) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.newsletterMember = newsletterMember;
		this.dateJoined = LocalDate.now();
		this.orderHistory = new ArrayList<>();
		this.enabled = enabled;
	}
}
