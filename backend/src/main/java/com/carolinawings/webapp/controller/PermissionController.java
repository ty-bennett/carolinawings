package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.config.ApplicationConstants;
import com.carolinawings.webapp.dto.PermissionDTO;
import com.carolinawings.webapp.dto.PermissionResponse;
import com.carolinawings.webapp.service.PermissionServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class PermissionController {

    private final PermissionServiceImplementation permissionServiceImplementation;

    public PermissionController(PermissionServiceImplementation permissionServiceImplementation) {
        this.permissionServiceImplementation = permissionServiceImplementation;
    }

    // Get all permissions
    @GetMapping("/permissions/all")
    public ResponseEntity<PermissionResponse> getPermissions() {
        return new ResponseEntity<>(permissionServiceImplementation.getAllPermissions(), HttpStatus.OK);
    }

    // Get all permissions with pagination
    @GetMapping("/permissions")
    public ResponseEntity<PermissionResponse> getPermissions(
            @RequestParam(name = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize) {
        return new ResponseEntity<>(permissionServiceImplementation.getAllPermissionsPaged(pageNumber, pageSize), HttpStatus.OK);
    }

    // Get permission by ID
    @GetMapping("/permissions/{id}")
    public ResponseEntity<Optional<PermissionDTO>> getPermission (@PathVariable Long id) {
        return new ResponseEntity<>(permissionServiceImplementation.getPermission(id), HttpStatus.OK);
    }

    // Create a new permission
    @PostMapping("/permissions")
    public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody PermissionDTO permissionDTO) {
        PermissionDTO savedPermission = permissionServiceImplementation.createPermission(permissionDTO);
        return new ResponseEntity<>(savedPermission, HttpStatus.CREATED);
    }

    // Delete a permission by ID
    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<PermissionDTO> deletePermission(@PathVariable Long id) {
        PermissionDTO deletedPermission = permissionServiceImplementation.deletePermission(id);
        return new ResponseEntity<>(deletedPermission, HttpStatus.OK);
    }

    // Update a permission
    @PutMapping("/permissions/{id}")
    public ResponseEntity<PermissionDTO> updatePermission(@Valid @RequestBody PermissionDTO permissionDTO,
                                                          @PathVariable Long id) {
        PermissionDTO updatedPermission = permissionServiceImplementation.updatePermission(permissionDTO, id);
        return new ResponseEntity<>(updatedPermission, HttpStatus.OK);
    }
}
