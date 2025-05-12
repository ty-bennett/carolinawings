package com.carolinawings.webapp.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table(name="customers")
public class Customer implements UserDetails{
	//Identifying information
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	@Getter
	@Setter
	@Nonnull
	private UUID id;
	@Getter
	@Setter
	@NonNull
	@Column(nullable = false)
	private String name;
	@Getter
	@Setter
	@Nonnull
	private String email;
	@Getter
	@Setter
    @Nonnull
	private String password;
	@Getter
	@Setter
	private String phoneNumber;
	//Are they a member of mailing list
	@Getter
	@Setter
	@Nonnull 
	private Boolean newsletterMember;
	//keep track of how old account is
	@Getter
	@Setter
	@Nonnull 
	private LocalDate dateJoined;
	//Keep track of orders with a list of orders
	@Getter
	@Setter
	@OneToMany(mappedBy = "customers", cascade = CascadeType.ALL)

	private List<CustomerOrder> orderHistory = new ArrayList<CustomerOrder>();

	//implemented methods for UserDetails service
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
	//implemented methods for username
	@Override
	public String getUsername() {
		return this.email;
	}
}
