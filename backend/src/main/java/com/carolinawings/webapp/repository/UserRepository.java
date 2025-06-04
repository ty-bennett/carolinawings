package com.carolinawings.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.carolinawings.webapp.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findAll();
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
}
