/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.repository;

import com.carolinawings.webapp.model.Role;
import com.carolinawings.webapp.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
