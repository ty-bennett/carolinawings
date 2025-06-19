package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.service.CompanyServiceImplementation;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carolinawings.webapp.model.Company;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class CompanyController {
    private final CompanyServiceImplementation companyServiceImplementation;

    public CompanyController(CompanyServiceImplementation companyServiceImplementation) {
        this.companyServiceImplementation = companyServiceImplementation;
    }

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getCompanies() {

        return new ResponseEntity<>(companyServiceImplementation.getAllCompanies(), HttpStatus.OK);
    }

    //gets a company by its id and returns a list to the user
    @GetMapping("/companies/{id}")
    public ResponseEntity<Optional<Company>> getCompanyById(@PathVariable Long id)
    {
        return new ResponseEntity<>(companyServiceImplementation.getCompanyById(id), HttpStatus.OK);
    }

    @PostMapping("/admin/companies")
    public ResponseEntity<String> createCompany(@RequestBody Company c)
    {
        companyServiceImplementation.createCompany(c);
        return new ResponseEntity<>("Company created successfully with id: "+c.getId(), HttpStatus.CREATED);
    }
    @DeleteMapping("/admin/companies/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id)
    {
        try {
            if(companyServiceImplementation.deleteCompanyById(id)) {
                return new ResponseEntity<>(companyServiceImplementation.getCompanyById(id).toString()+" deleted successfully", HttpStatus.OK);
            }
            else return new ResponseEntity<>("Company not found with id "+ id, HttpStatus.NOT_FOUND);
            } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/admin/companies/{id}")
    public ResponseEntity<String> updateCompany(@RequestBody Company company,
                                                @PathVariable Long id)
    {
        try {
            companyServiceImplementation.updateCompany(company, id);
            return new ResponseEntity<>("Company edited with existing id:" + id, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
