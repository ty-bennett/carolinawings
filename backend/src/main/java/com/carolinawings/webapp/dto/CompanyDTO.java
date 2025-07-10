package com.carolinawings.webapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CompanyDTO {
    private String name;
    private String address;
    private String industry;
    private String phoneNumber;
}
