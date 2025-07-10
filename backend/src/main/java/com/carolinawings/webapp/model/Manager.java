/*
 * Ty Bennett
 */
package com.carolinawings.webapp.model;

//Imports 
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="managers")
public class Manager {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long managerId;
	private String email;
	private String name;
	private String password;
	private Double phoneNumber;

	public Manager() {}

	public Manager(String name, String password, Double phoneNumber, String location) {
		this.name = name;
		this.password = password;
		this.phoneNumber = phoneNumber;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Double getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Double phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public boolean equals(Manager m) {
		return m != null &&
				this.managerId.equals(m.managerId)
				&& this.name.equals(m.name)
				&& this.password.equals(m.password)
				&& this.phoneNumber.equals(m.phoneNumber);
	}

	public String toString() {
		return "Manager{" +
				"managerId=" + managerId +
				", email='" + email + '\'' +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				", phoneNumber=" + phoneNumber +
				'}';
	}
}
