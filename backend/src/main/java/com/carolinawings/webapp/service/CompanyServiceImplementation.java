/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.CompanyDTO;
import com.carolinawings.webapp.dto.CompanyResponse;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Company;
import com.carolinawings.webapp.repository.CompanyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImplementation implements CompanyService {

    private final CompanyRepository companyRepository;
    @Autowired
    private ModelMapper modelMapper;

    public CompanyServiceImplementation(CompanyRepository companyRepository)
    {
        this.companyRepository = companyRepository;
    }

    @Override
    public CompanyResponse getAllCompanies()
    {
        List<Company> companies = companyRepository.findAll();
        if(companies.isEmpty())
            throw new APIException("No companies present");
        List<CompanyDTO> companyDTOS = companies.stream()
                .map(company -> modelMapper.map(company, CompanyDTO.class))
                .toList();

        return new CompanyResponse(companyDTOS);
    }

    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company company = modelMapper.map(companyDTO, Company.class);
        Company savedCompany = companyRepository.findByName(company.getName());
        if(savedCompany != null)
            throw new APIException("Company with the name "+ company.getName() + " already exists");
        Company returnCompany = companyRepository.save(company);
        return modelMapper.map(returnCompany, CompanyDTO.class);
    }

    @Override
    public Optional<CompanyDTO> getCompanyById(Long id) {
        Optional<Company> company = companyRepository.findById(id);
        return company.map((element) -> modelMapper.map(element, CompanyDTO.class));
    }

    @Override
    public CompanyDTO deleteCompanyById(Long id) {
        Company c = companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Company", "companyId", id));
        companyRepository.delete(c);
        return modelMapper.map(c, CompanyDTO.class);
    }


    @Override
    public CompanyDTO updateCompany(CompanyDTO companyDTO, Long id) {
        Company savedCompany = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "companyId: ", id));
        Company company = modelMapper.map(companyDTO, Company.class);
        company.setId(id);
        Company savedCompanyToRepo = companyRepository.save(company);
        return modelMapper.map(savedCompanyToRepo, CompanyDTO.class);
    }
}