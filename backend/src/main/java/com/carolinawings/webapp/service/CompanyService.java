package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.Company;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    List<Company> getAllCompanies();
    ResponseEntity<Optional<Company>> getCompanyById(Long id);
    ResponseEntity<Company> createCompany(Company company);
    boolean deleteById(Long id);
}
