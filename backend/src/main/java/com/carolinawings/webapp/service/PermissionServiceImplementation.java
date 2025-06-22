/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.Permission;
import com.carolinawings.webapp.repository.PermissionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImplementation implements PermissionService{

    private PermissionRepository permissionRepository;

    public PermissionServiceImplementation(PermissionRepository permissionRepository) {
        this.permissionRepository= permissionRepository;
    }

    @Override
    public List<Permission> getPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Optional<Permission> getPermissionById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public String createPermission(Permission permission) {
        permissionRepository.save(permission);
        return "Role created with id: " + permission.getId();
    }

    @Override
    public String deletePermission(Long id) {
        if(!permissionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }
        permissionRepository.deleteById(id);
        return "Role deleted with id: " + id;
    }

    @Override
    public Permission updatePermission(Permission permission, Long id) {
        return permissionRepository.findById(id)
                .map(existingPermission -> {
                    existingPermission.setName(permission.getName());
                    existingPermission.setDescription(permission.getDescription());
                    existingPermission.setRolesMember(permission.getRolesMember());
                    return permissionRepository.save(existingPermission);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
    }
}
