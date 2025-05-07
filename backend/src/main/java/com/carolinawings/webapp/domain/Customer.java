package com.carolinawings.webapp.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class Customer implements UserDetails{
	private static final long serialVersionUID = 7309123450785917473L;
	//Identifying information
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	private String password;
	private String phoneNumber;
	//Are they a member of mailing list
	private Boolean newsletterMember;
	//keep track of how old account is
	private LocalDate dateJoined;	
	//Keep track of orders with a list of orders
	@OneToMany()
	private List<CustomerOrder> orderHistory = new ArrayList<CustomerOrder>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return name;
	}
	public void setUsername(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Boolean getNewsletterMember() {
		return newsletterMember;
	}
	public void setNewsletterMember(Boolean newsletterMember) {
		this.newsletterMember = newsletterMember;
	}
	public LocalDate getDateJoined() {
		return dateJoined;
	}
	public void setDateJoined(LocalDate dateJoined) {
		this.dateJoined = dateJoined;
	}
	public List<CustomerOrder> getOrderHistory() {
		return orderHistory;
	}
	public void setOrderHistory(List<CustomerOrder> orderHistory) {
		this.orderHistory = orderHistory;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}
}
