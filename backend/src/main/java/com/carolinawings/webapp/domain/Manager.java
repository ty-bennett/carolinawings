/*
 * Ty Bennett
 */
package com.carolinawings.webapp.domain;

//Imports 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="managers")
public class Manager {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long managerId;
	private String name;
	private String password;
	private Double phoneNumber;
	private String location;

	public Manager() {}

	public Manager(String name, String password, Double phoneNumber, String location) {
		this.name = name;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.location = location;
	}

}
