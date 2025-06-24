/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByName(String name);
}
