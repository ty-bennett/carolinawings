/*
Written by Ty Bennett
*/
package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.PermissionDTO;
import com.carolinawings.webapp.dto.PermissionResponse;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Permission;
import com.carolinawings.webapp.repository.PermissionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImplementation implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PermissionServiceImplementation(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public PermissionResponse getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        if (permissions.isEmpty())
            throw new APIException("No permissions present");

        List<PermissionDTO> permissionDTOS = permissions.stream()
                .map(permission -> modelMapper.map(permission, PermissionDTO.class))
                .toList();

        return new PermissionResponse(permissionDTOS);
    }

    @Override
    public PermissionResponse getAllPermissionsPaged(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Permission> permissionsPage = permissionRepository.findAll(pageable);

        List<Permission> permissions = permissionsPage.getContent();
        if (permissions.isEmpty())
            throw new APIException("No permissions present");

        List<PermissionDTO> permissionDTOS = permissions.stream()
                .map(permission -> modelMapper.map(permission, PermissionDTO.class))
                .toList();

        PermissionResponse response = new PermissionResponse();
        response.setContent(permissionDTOS);
        response.setPageNumber(permissionsPage.getNumber());
        response.setPageSize(permissionsPage.getSize());
        response.setTotalElements(permissionsPage.getTotalElements());
        response.setTotalPages(permissionsPage.getTotalPages());
        response.setLastPage(permissionsPage.isLast());

        return response;
    }

    @Override
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        Permission permission = modelMapper.map(permissionDTO, Permission.class);
        Permission existing = permissionRepository.findByName(permission.getName());
        if (existing != null)
            throw new APIException("Permission with the name " + permission.getName() + " already exists");

        Permission saved = permissionRepository.save(permission);
        return modelMapper.map(saved, PermissionDTO.class);
    }

    @Override
    public Optional<PermissionDTO> getPermission(Long id) {
        Optional<Permission> permission = permissionRepository.findById(id);
        return permission.map(p -> modelMapper.map(p, PermissionDTO.class));
    }

    @Override
    public PermissionDTO deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", "permissionId", id));
        permissionRepository.delete(permission);
        return modelMapper.map(permission, PermissionDTO.class);
    }

    @Override
    public PermissionDTO updatePermission(PermissionDTO permissionDTO, Long id) {
        Permission existing = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", "permissionId", id));

        Permission updated = modelMapper.map(permissionDTO, Permission.class);
        updated.setId(id);
        Permission saved = permissionRepository.save(updated);

        return modelMapper.map(saved, PermissionDTO.class);
    }
}
