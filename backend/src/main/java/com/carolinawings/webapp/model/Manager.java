/*
 * Ty Bennett
 */
package com.carolinawings.webapp.model;

//Imports 
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="managers")
public class Manager {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long managerId;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private Double phoneNumber;
	@Column(nullable = false)
	private String location;

	public Manager() {}

	public Manager(String name, String password, Double phoneNumber, String location) {
		this.name = name;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.location = location;
	}
}
