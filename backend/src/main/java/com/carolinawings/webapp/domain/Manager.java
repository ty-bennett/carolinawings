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

@Entity
@Table(name="managers")
public class Manager {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long managerid;
	private String name;
	private String password;
	private Double phoneNumber;
	private String location;
	
	public Long getManagerid() {
		return managerid;
	}
	public void setManagerid(Long managerid) {
		this.managerid = managerid;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}
