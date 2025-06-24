/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.repository;

import jakarta.validation.constraints.NotBlank;
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
    Company findByName(@NotBlank String name);
}
