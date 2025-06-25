package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.config.ApplicationConstants;
import com.carolinawings.webapp.dto.RoleDTO;
import com.carolinawings.webapp.dto.RoleResponse;
import com.carolinawings.webapp.service.RoleServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class RoleController {
    private final RoleServiceImplementation roleServiceImplementation;

    public RoleController(RoleServiceImplementation roleServiceImplementation) {
        this.roleServiceImplementation = roleServiceImplementation;
    }

    // Get all roles
    @GetMapping("/roles/all")
    public ResponseEntity<RoleResponse> getRoles() {
        return new ResponseEntity<>(roleServiceImplementation.getAllRoles(), HttpStatus.OK);
    }

    // Get all roles paged
    @GetMapping("/roles")
    public ResponseEntity<RoleResponse> getRoles(
            @RequestParam(name = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize) {
        return new ResponseEntity<>(roleServiceImplementation.getAllRolesPaged(pageNumber, pageSize), HttpStatus.OK);
    }

    // Get role by id
    @GetMapping("/roles/{id}")
    public ResponseEntity<Optional<RoleDTO>> getRoleById(@PathVariable Long id) {
        return new ResponseEntity<>(roleServiceImplementation.getRoleById(id), HttpStatus.OK);
    }

    // Create role
    @PostMapping("/roles")
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO roleDTO) {
        RoleDTO savedRoleDTO = roleServiceImplementation.createRole(roleDTO);
        return new ResponseEntity<>(savedRoleDTO, HttpStatus.CREATED);
    }

    // Delete role by id
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<RoleDTO> deleteRoleById(@PathVariable Long id) {
        RoleDTO deletedRole = roleServiceImplementation.deleteRole(id);
        return new ResponseEntity<>(deletedRole, HttpStatus.OK);
    }

    // Update role by id
    @PutMapping("/roles/{id}")
    public ResponseEntity<RoleDTO> updateRole(@Valid @RequestBody RoleDTO roleDTO,
                                              @PathVariable Long id) {
        RoleDTO updatedRoleDTO = roleServiceImplementation.updateRole(roleDTO, id);
        return new ResponseEntity<>(updatedRoleDTO, HttpStatus.OK);
    }
}
