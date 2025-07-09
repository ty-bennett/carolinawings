/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.RoleDTO;
import com.carolinawings.webapp.dto.RoleResponse;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Role;
import com.carolinawings.webapp.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImplementation implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    public RoleServiceImplementation(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleResponse getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty())
            throw new APIException("No roles present");
        List<RoleDTO> roleDTOS = roles.stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .toList();

        return new RoleResponse(roleDTOS);
    }

    public RoleResponse getAllRolesPaged(Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Role> roles = roleRepository.findAll(pageDetails);
        List<Role> rolesPageable = roles.getContent();
        if (rolesPageable.isEmpty())
            throw new APIException("No roles present");
        List<RoleDTO> roleDTOS = rolesPageable.stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .toList();
        RoleResponse rR = new RoleResponse();
        rR.setContent(roleDTOS);
        rR.setPageNumber(roles.getNumber());
        rR.setPageSize(roles.getSize());
        rR.setTotalElements(roles.getTotalElements());
        rR.setTotalPages(roles.getTotalPages());
        rR.setLastPage(roles.isLast());
        return rR;
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = modelMapper.map(roleDTO, Role.class);
        Role savedRole = roleRepository.findByName(role.getName().name())
                .orElseThrow(() -> new APIException("Role with the name " + role.getName() + " already exists"));
        Role returnRole = roleRepository.save(role);
        return modelMapper.map(returnRole, RoleDTO.class);
    }

    @Override
    public Optional<RoleDTO> getRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.map(element -> modelMapper.map(element, RoleDTO.class));
    }

    @Override
    public RoleDTO deleteRole(Long id) {
        Role r = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role", "roleId", id));
        roleRepository.delete(r);
        return modelMapper.map(r, RoleDTO.class);
    }

    @Override
    public RoleDTO updateRole(RoleDTO roleDTO, Long id) {
        Role savedRole = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "roleId", id));
        Role role = modelMapper.map(roleDTO, Role.class);
        role.setId(id);
        Role savedRoleToRepo = roleRepository.save(role);
        return modelMapper.map(savedRoleToRepo, RoleDTO.class);
    }
}
