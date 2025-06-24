/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Company;
import com.carolinawings.webapp.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImplementation implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImplementation(CompanyRepository companyRepository)
    {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> getAllCompanies()
    {
        List<Company> companies = companyRepository.findAll();
        if(companies.isEmpty())
            throw new APIException("No companies present");
        return companies;
    }

    @Override
    public String createCompany(Company company) {
        Company savedCompany = companyRepository.findByName(company.getName());
        if(savedCompany != null)
            throw new APIException("Company with the name "+ company.getName() + " already exists");
        companyRepository.save(company);
        return "Company with id " +company.getId()+" added successfully";
    }

    @Override
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public String deleteCompanyById(Long id) {
        Company c = companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Company", "companyId", id));
        companyRepository.delete(c);
        return "Company with id " + id + " deleted successfully";
    }


    @Override
    public Company updateCompany(Company company, Long id) {
        Company savedCompany = companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Company", "companyId: ", id));
        company.setId(id);
        return companyRepository.save(savedCompany);
    }
}