package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.Permission;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PermissionService {
    List<Permission> getPermissions();
    Optional<Permission> getPermissionById(Long id);
    String createPermission(Permission permission);
    String deletePermission(Long id);
    Permission updatePermission(Permission permission, Long id);
}
