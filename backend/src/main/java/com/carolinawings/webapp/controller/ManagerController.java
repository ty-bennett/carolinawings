package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.config.ApplicationConstants;
import com.carolinawings.webapp.dto.ManagerDTO;
import com.carolinawings.webapp.dto.ManagerResponse;
import com.carolinawings.webapp.service.ManagerServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class ManagerController {

    private final ManagerServiceImplementation managerServiceImplementation;

    public ManagerController(ManagerServiceImplementation managerServiceImplementation) {
        this.managerServiceImplementation = managerServiceImplementation;
    }

    // Get all managers
    @GetMapping("/managers/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ManagerResponse> getManagers() {
        return new ResponseEntity<>(managerServiceImplementation.getAllManagers(), HttpStatus.OK);
    }

    // Get all managers with pagination
    @GetMapping("/managers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ManagerResponse> getManagers(
            @RequestParam(name = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize) {
        return new ResponseEntity<>(managerServiceImplementation.getAllManagersPage(pageNumber, pageSize), HttpStatus.OK);
    }

    // Get a manager by id
    @GetMapping("/managers/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Optional<ManagerDTO>> getManagerById(@PathVariable Long id) {
        return new ResponseEntity<>(managerServiceImplementation.getManagerById(id), HttpStatus.OK);
    }

    // Create a new manager
    @PostMapping("/managers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ManagerDTO> createManager(@Valid @RequestBody ManagerDTO managerDTO) {
        ManagerDTO savedManager = managerServiceImplementation.createManager(managerDTO);
        return new ResponseEntity<>(savedManager, HttpStatus.CREATED);
    }

    // Delete a manager by id
    @DeleteMapping("/managers/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ManagerDTO> deleteManagerById(@PathVariable Long id) {
        ManagerDTO deletedManager = managerServiceImplementation.deleteManager(id);
        return new ResponseEntity<>(deletedManager, HttpStatus.OK);
    }

    // Update a manager by id
    @PutMapping("/managers/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ManagerDTO> updateManager(@Valid @RequestBody ManagerDTO managerDTO,
                                                    @PathVariable Long id) {
        ManagerDTO updatedManager = managerServiceImplementation.updateManager(managerDTO, id);
        return new ResponseEntity<>(updatedManager, HttpStatus.OK);
    }
}
