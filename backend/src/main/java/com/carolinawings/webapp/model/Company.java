/*
Ty Bennett
*/

package com.carolinawings.webapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "companies")
@Data

public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    private String logoURL;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String industry;

    public Company() {}

    public Company(String name, String address, String logoURL, String phoneNumber, String industry) {
        this.name = name;
        this.address = address;
        this.logoURL = logoURL;
        this.phoneNumber = phoneNumber;
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
}
