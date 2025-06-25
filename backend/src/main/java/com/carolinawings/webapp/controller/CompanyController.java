package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.dto.CompanyDTO;
import com.carolinawings.webapp.dto.CompanyResponse;
import com.carolinawings.webapp.repository.CompanyRepository;
import com.carolinawings.webapp.service.CompanyServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carolinawings.webapp.model.Company;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class CompanyController {
    private final CompanyServiceImplementation companyServiceImplementation;

    public CompanyController(CompanyServiceImplementation companyServiceImplementation, CompanyRepository companyRepository) {
        this.companyServiceImplementation = companyServiceImplementation;
    }


    @GetMapping("/echo")
    public ResponseEntity<String> echoMessage(@RequestParam(name="message", required = false) String message) {
        //public ResponseEntity<String> echoMessage (@RequestParam(name="message", defaultValue="Hello carolina wings") String message) {
        return new ResponseEntity<>("Echoed message: "+ message, HttpStatus.OK);
    }



    @GetMapping("/companies")
    public ResponseEntity<CompanyResponse> getCompanies() {
        return new ResponseEntity<>(companyServiceImplementation.getAllCompanies(),HttpStatus.OK);
    }

    //gets a company by its id and returns a list to the user
    @GetMapping("/companies/{id}")
    public ResponseEntity<Optional<CompanyDTO>> getCompanyById(@PathVariable Long id)
    {
        return new ResponseEntity<>(companyServiceImplementation.getCompanyById(id), HttpStatus.OK);
    }

    @PostMapping("/companies")
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CompanyDTO c)
    {
        CompanyDTO savedCompanyDTO = companyServiceImplementation.createCompany(c);
        return new ResponseEntity<>(savedCompanyDTO, HttpStatus.CREATED);
    }
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<CompanyDTO> deleteCompanyId(@PathVariable Long id)
    {
        CompanyDTO deletedCompany = companyServiceImplementation.deleteCompanyById(id);
        return new ResponseEntity<>(deletedCompany, HttpStatus.OK);
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@Valid @RequestBody CompanyDTO companyDTO,
                                                @PathVariable Long id)
    {
            CompanyDTO savedCompanyDTO = companyServiceImplementation.updateCompany(companyDTO, id);
            return new ResponseEntity<>(savedCompanyDTO, HttpStatus.OK);
    }
}
