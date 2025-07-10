/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String username);

    boolean existsByEmail(@Email(message = "Email should be valid") @Size(max=50, min = 7, message = "Email must be a valid email") String email);
}
