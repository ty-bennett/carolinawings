/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.RoleDTO;
import com.carolinawings.webapp.dto.RoleResponse;

import java.util.Optional;

public interface RoleService {
    RoleResponse getAllRoles();
    RoleResponse getAllRolesPaged(Integer page, Integer pageSize);
    Optional<RoleDTO> getRoleById(Long id);
    RoleDTO createRole(RoleDTO role);
    RoleDTO deleteRole(Long id);
    RoleDTO updateRole(RoleDTO role, Long id);
}
