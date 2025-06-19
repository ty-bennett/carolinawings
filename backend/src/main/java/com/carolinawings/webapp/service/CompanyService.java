/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    List<Company> getAllCompanies();
    Optional<Company> getCompanyById(Long id);
    String createCompany(Company company);
    String deleteCompanyById(Long id);
    Company updateCompany(Company company, Long id);
}
