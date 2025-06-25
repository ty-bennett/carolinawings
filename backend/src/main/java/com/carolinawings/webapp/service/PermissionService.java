/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.PermissionDTO;
import com.carolinawings.webapp.dto.PermissionResponse;

import java.util.Optional;

public interface PermissionService {
    PermissionResponse getAllPermissions();
    PermissionResponse getAllPermissionsPaged(Integer page, Integer pageSize);
    Optional<PermissionDTO> getPermission(Long id);
    PermissionDTO createPermission(PermissionDTO permission);
    PermissionDTO deletePermission(Long id);
    PermissionDTO updatePermission(PermissionDTO permission, Long id);
}
