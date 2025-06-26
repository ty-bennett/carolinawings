package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.config.ApplicationConstants;
import com.carolinawings.webapp.dto.CompanyDTO;
import com.carolinawings.webapp.dto.CompanyResponse;
import com.carolinawings.webapp.service.CompanyServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/platform-admin")
public class CompanyController {
    private final CompanyServiceImplementation companyServiceImplementation;

    public CompanyController(CompanyServiceImplementation companyServiceImplementation) {
        this.companyServiceImplementation = companyServiceImplementation;
    }

    //Get all companies and paginate results

    @GetMapping("/companies")
    public ResponseEntity<CompanyResponse> getCompanies(
            @RequestParam(name="pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name="pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize ) {
        return new ResponseEntity<>(companyServiceImplementation.getAllCompaniesPaged(pageNumber, pageSize),HttpStatus.OK);
    }

    //gets a company by its id and returns a list to the user
    @GetMapping("/companies/{id}")
    public ResponseEntity<Optional<CompanyDTO>> getCompanyById(@PathVariable Long id)
    {
        return new ResponseEntity<>(companyServiceImplementation.getCompanyById(id), HttpStatus.OK);
    }

    //Create a company with valid object in request
    @PostMapping("/companies")
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CompanyDTO c)
    {
        CompanyDTO savedCompanyDTO = companyServiceImplementation.createCompany(c);
        return new ResponseEntity<>(savedCompanyDTO, HttpStatus.CREATED);
    }

    //Delete a company using its id
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<CompanyDTO> deleteCompanyId(@PathVariable Long id)
    {
        CompanyDTO deletedCompany = companyServiceImplementation.deleteCompanyById(id);
        return new ResponseEntity<>(deletedCompany, HttpStatus.OK);
    }

    //change info in a company
    @PutMapping("/companies/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@Valid @RequestBody CompanyDTO companyDTO,
                                                @PathVariable Long id)
    {
            CompanyDTO savedCompanyDTO = companyServiceImplementation.updateCompany(companyDTO, id);
            return new ResponseEntity<>(savedCompanyDTO, HttpStatus.OK);
    }
}
