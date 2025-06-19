/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import com.carolinawings.webapp.model.Company;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

//Repository Annotation
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
//   //Find all companies endpoint
//    List<Company> findAllCompanies();
//    //Find a company by its id
//    Optional<Company> findCompanyById(Long id);
//    //delete a company by its id
//    void deleteCompanyById(Long id);
//    //save a company to the db
//    <S extends Company> S save(S entity);
//    //update a company and include a company in the body (PUT not PATCH)
//    Optional<Company> updateCompany(Company company);
}
