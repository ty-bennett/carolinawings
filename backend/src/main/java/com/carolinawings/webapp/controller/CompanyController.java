package com.carolinawings.webapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import com.carolinawings.webapp.model.Company;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CompanyController {
    private List<Company> companies = new ArrayList<>();

    @GetMapping("/companies")
    public List<Company> getCompanies() {
        return companies;
    }

    @PostMapping("/companies")
    public Company createCompany(@RequestBody Company c)
    {
        return c;
    }
}

