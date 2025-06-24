package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.repository.CompanyRepository;
import com.carolinawings.webapp.service.CompanyServiceImplementation;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carolinawings.webapp.model.Company;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class CompanyController {
    private final CompanyServiceImplementation companyServiceImplementation;

    public CompanyController(CompanyServiceImplementation companyServiceImplementation, CompanyRepository companyRepository) {
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

    @PostMapping("/companies")
    public ResponseEntity<String> createCompany(@Valid @RequestBody Company c)
    {
        companyServiceImplementation.createCompany(c);
        return new ResponseEntity<>("Company created successfully: \n" +c, HttpStatus.CREATED);
    }
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<String> deleteCompanyId(@PathVariable Long id)
    {
        return new ResponseEntity<>(companyServiceImplementation.deleteCompanyById(id), HttpStatus.OK);
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<String> updateCompany(@Valid @RequestBody Company company,
                                                @PathVariable Long id)
    {
            companyServiceImplementation.updateCompany(company, id);
            return new ResponseEntity<>("Company edited with existing id:" + id, HttpStatus.OK);
    }
}
