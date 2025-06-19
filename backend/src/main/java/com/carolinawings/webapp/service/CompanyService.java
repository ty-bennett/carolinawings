package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.Company;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    List<Company> getAllCompanies();
    Optional<Company> getCompanyById(Long id);
    String createCompany(Company company);
    boolean deleteCompanyById(Long id);
    Company updateCompany(Company company, Long id);
}
