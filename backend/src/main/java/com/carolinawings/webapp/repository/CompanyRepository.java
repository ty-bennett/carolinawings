package com.carolinawings.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.carolinawings.webapp.model.Company;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> getCompanies();
    Company getCompany();
    Optional<Company> getCompanyById(Long id);
    ResponseEntity deleteCompanyById(Long id);
}
