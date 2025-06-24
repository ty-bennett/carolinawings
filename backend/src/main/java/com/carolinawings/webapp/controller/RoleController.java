package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.model.Role;
import com.carolinawings.webapp.service.RoleServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class RoleController {
    private RoleServiceImplementation roleServiceImplementation;

    public RoleController(RoleServiceImplementation roleServiceImplementation) {
        this.roleServiceImplementation = roleServiceImplementation;
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles()
    {
        return new ResponseEntity<>(roleServiceImplementation.getAllRoles(), HttpStatus.OK);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Optional<Role>> getRoleById(@PathVariable Long id)
    {
        return new ResponseEntity<>(roleServiceImplementation.getRoleById(id), HttpStatus.OK);
    }

    @PostMapping("/roles")
    public ResponseEntity<String> createRole(@RequestBody Role r)
    {
        roleServiceImplementation.createRole(r);
        return new ResponseEntity<>("Permission created successfully \n" + r, HttpStatus.CREATED);
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id)
    {
        try {
            return new ResponseEntity<>(roleServiceImplementation.deleteRole(id), HttpStatus.OK);
        } catch (ResponseStatusException e)
        {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<String> updateRole(@RequestBody Role r,
                                                    @PathVariable Long id)
    {
        try {
            roleServiceImplementation.updateRole(r, id);
            return new ResponseEntity<>("Edited manager with id: "+id+" \n" +r, HttpStatus.OK);
        } catch(ResponseStatusException e)
        {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
