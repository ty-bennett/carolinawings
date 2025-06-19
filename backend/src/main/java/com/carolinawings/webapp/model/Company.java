/*
Ty Bennett
*/

package com.carolinawings.webapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String logoURL;
    private String phoneNumber;
    private String industry;

    public Company() {}

    public Company(String name, String address, String logoURL, String phoneNumber, String industry) {
        this.name = name;
        this.address = address;
        this.logoURL = logoURL;
        this.phoneNumber = phoneNumber;
        this.industry = industry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", logoURL='" + logoURL + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", industry='" + industry + '\'' +
                '}';
    }

    public boolean equals(Company c) {
        return c != null &&
                this.getId().equals(c.getId()) &&
                this.getName().equals(c.getName()) &&
                this.getAddress().equals(c.getAddress()) &&
                this.getLogoURL().equals(c.getLogoURL()) &&
                this.getPhoneNumber().equals(c.getPhoneNumber()) &&
                this.getIndustry().equals(c.getIndustry());
    }
}
