package com.carolinawings.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.carolinawings.webapp.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findById(UUID id);
}
