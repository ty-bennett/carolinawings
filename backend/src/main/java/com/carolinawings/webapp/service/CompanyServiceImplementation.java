package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.Company;
import com.carolinawings.webapp.repository.CompanyRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImplementation implements CompanyService {

    private final CompanyRepository companyRepository;
    @Autowired
    public CompanyServiceImplementation(CompanyRepository companyRepository)
    {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public ResponseEntity<Company> createCompany(Company company) {
        if(companyRepository.existsById(company.getId()))
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        return new ResponseEntity<Company>(companyRepository.save(company), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Optional<Company>> getCompanyById(Long id) {
        return new ResponseEntity<>(companyRepository.findCompanyById(id), HttpStatus.OK);
    }

    public boolean deleteById(Long id)
    {
        if(companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}