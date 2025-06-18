package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.service.CompanyServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carolinawings.webapp.model.Company;

import java.util.List;
import java.util.Optional;

@RestController
public class CompanyController {
    private final CompanyServiceImplementation companyServiceImplementation;

    public CompanyController(CompanyServiceImplementation companyServiceImplementation) {
        this.companyServiceImplementation = companyServiceImplementation;
    }

    //TODO: create company service
    @GetMapping("/companies")
    public List<Company> getCompanies() {

       //TODO: reflect creation of service with methods
        return companyServiceImplementation.getAllCompanies();
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Optional<Company>> getCompanyById(@PathVariable Long id)
    {
        return companyServiceImplementation.getCompanyById(id);
    }

    @PostMapping("/admin/companies")
    public ResponseEntity<Company> createCompany(@RequestBody Company c)
    {
        return companyServiceImplementation.createCompany(c);
    }
    @DeleteMapping("/admin/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id)
    {
        boolean deleted = companyServiceImplementation.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build();  // 404 Not Found
        }
    }
}
