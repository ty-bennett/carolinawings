/*
written by Ty Bennett
 */

package com.carolinawings.webapp.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CollectionIdMutability;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table(name="users")
@Getter
@Setter
public class User implements UserDetails {
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
	private List<CustomerOrder> orderHistory = new ArrayList<>();
	//give verification code upon signup
	@Column(nullable = false, name = "verification_code")
	private String verificationCode;
	@Column(name = "verification_expiration")
	private LocalDateTime verificationExpiresAt;
	private boolean enabled;

	public User() {}

	public User(String name, String email, String password, String phoneNumber, Boolean newsletterMember) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.newsletterMember = newsletterMember;
		this.dateJoined = LocalDate.now();
		this.orderHistory = new ArrayList<>();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

    @Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
}
