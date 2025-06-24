/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Permission;
import com.carolinawings.webapp.repository.PermissionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImplementation implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImplementation(PermissionRepository permissionRepository)
    {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<Permission> getPermissions()
    {
        List<Permission> permissions = permissionRepository.findAll();
        if(permissions.isEmpty())
            throw new APIException("No permissions present");
        return permissions;
    }

    @Override
    public String createPermission(Permission permission) {
        Permission savedPermission = permissionRepository.findByName(permission.getName());
        if(savedPermission != null)
            throw new APIException("Permission with the name " + permission.getName() + " already exists");
        permissionRepository.save(permission);
        return "Permission with id " + permission.getId() + " added successfully";
    }

    @Override
    public Optional<Permission> getPermissionById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public String deletePermission(Long id) {
        Permission p = permissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Permission", "permissionId", id));
        permissionRepository.delete(p);
        return "Permission with id " + id + " deleted successfully";
    }

    @Override
    public Permission updatePermission(Permission permission, Long id) {
        Permission savedPermission = permissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Permission", "permissionId", id));
        permission.setId(id);
        return permissionRepository.save(savedPermission);
    }
}
