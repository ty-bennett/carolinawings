/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Role;
import com.carolinawings.webapp.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImplementation implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImplementation(RoleRepository roleRepository)
    {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles()
    {
        List<Role> roles = roleRepository.findAll();
        if(roles.isEmpty())
            throw new APIException("No roles present");
        return roles;
    }

    @Override
    public String createRole(Role role) {
        Role savedRole = roleRepository.findByName(role.getName());
        if(savedRole != null)
            throw new APIException("Role with the name " + role.getName() + " already exists");
        roleRepository.save(role);
        return "Role with id " + role.getId() + " added successfully";
    }

    @Override
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public String deleteRole(Long id) {
        Role r = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role", "roleId", id));
        roleRepository.delete(r);
        return "Role with id " + id + " deleted successfully";
    }

    @Override
    public Role updateRole(Role role, Long id) {
        Role savedRole = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role", "roleId", id));
        role.setId(id);
        return roleRepository.save(savedRole);
    }
}
