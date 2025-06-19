package com.carolinawings.webapp.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import com.carolinawings.webapp.model.Company;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findAll();
    Optional<Company> findCompanyById(Long id);
    void deleteById(Long id);
    <S extends Company> S save(S entity);
    boolean existsByName(String name);
}
