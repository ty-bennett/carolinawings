package com.carolinawings.webapp.service;

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
    public String createCompany(Company company) {
        companyRepository.save(company);
        return "Company with id " +company.getId()+" added successfully";
    }

    @Override
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findCompanyById(id);
    }

    @Override
    public boolean deleteCompanyById(Long id)
    {
        if(companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Company updateCompany(Company company, Long id)
    {
        Optional<Company> companyToUpdate = companyRepository.findAll().stream()
            .filter(c -> c.getId().equals(id))
            .findFirst();

        if(companyToUpdate.isPresent())
        {
            Company existingCompany = companyToUpdate.get();
            existingCompany.setName(company.getName());
            existingCompany.setId(company.getId());
            existingCompany.setAddress(company.getAddress());
            existingCompany.setLogoURL(company.getLogoURL());
            existingCompany.setPhoneNumber(company.getPhoneNumber());
            existingCompany.setIndustry(company.getIndustry());
            return existingCompany;
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
    }
}