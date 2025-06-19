/*
Written by Ty Bennett
*/


package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.Role;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface RoleService {
    List<Role> getAllRoles();
    Optional<Role> getRoleById(Long id);
    String createRole(Role role);
    String deleteRole(Long id);
    Role updateRole(Role role, Long id);
}
