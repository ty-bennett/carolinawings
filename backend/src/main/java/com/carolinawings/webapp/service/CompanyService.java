/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.CompanyDTO;
import com.carolinawings.webapp.dto.CompanyResponse;
import com.carolinawings.webapp.model.Company;

import java.util.Optional;

public interface CompanyService {
    CompanyResponse getAllCompanies();
    Optional<CompanyDTO> getCompanyById(Long id);
    CompanyDTO createCompany(CompanyDTO company);
    CompanyDTO deleteCompanyById(Long id);
    CompanyDTO updateCompany(CompanyDTO company, Long id);
}
