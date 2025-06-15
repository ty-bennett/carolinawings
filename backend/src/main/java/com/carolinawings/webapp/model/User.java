/*
Written by Ty Bennett
 */

package com.carolinawings.webapp.model;

import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {
	//Identifying information
    @Id
	@GeneratedValue(strategy = GenerationType.UUID)
	//UUID of user
	private UUID id;
	//name of user
	private String name;
	//email of users
	private String email;
	private String password;
	private String phoneNumber;
	//Are they a member of mailing list
	private Boolean newsletterMember;
	//keep track of how old account is
	private LocalDate dateJoined;
	//set status of User
	private Boolean enabled;

	public User() {}

	public User(String name, String email, String password, String phoneNumber, Boolean newsletterMember, Boolean enabled) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.newsletterMember = newsletterMember;
		this.dateJoined = LocalDate.now();
		this.enabled = enabled;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
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

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", newsletterMember=" + newsletterMember +
				", dateJoined=" + dateJoined +
				", enabled=" + enabled +
				'}';
	}

	public boolean equals(User u) {
        return u != null &&
				this.id.equals(u.id) &&
				this.name.equals(u.name) &&
				this.email.equals(u.email) &&
				this.password.equals(u.password) &&
				this.phoneNumber.equals(u.phoneNumber) &&
				this.newsletterMember.equals(u.newsletterMember) &&
				this.dateJoined.equals(u.dateJoined) &&
				this.enabled.equals(u.enabled);
	}
}
