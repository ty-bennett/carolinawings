/*
Ty Bennett
*/

package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.service.PermissionServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.carolinawings.webapp.model.Permission;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class PermissionController{
    private final PermissionServiceImplementation permissionServiceImplementation;

    public PermissionController(PermissionServiceImplementation permissionServiceImplementation)
    {
        this.permissionServiceImplementation = permissionServiceImplementation;
    }

    @GetMapping("/permissions")
    public ResponseEntity<List<Permission>> getPermissions()
    {
        return new ResponseEntity<>(permissionServiceImplementation.getPermissions(), HttpStatus.OK);
    }

    @GetMapping("/permissions/{id}")
    public ResponseEntity<Optional<Permission>> getPermissionById(@PathVariable Long id)
    {
        return new ResponseEntity<>(permissionServiceImplementation.getPermissionById(id), HttpStatus.OK);
    }

    @PostMapping("/permissions")
    public ResponseEntity<String> createManager(@RequestBody Permission p)
    {
        permissionServiceImplementation.createPermission(p);
        return new ResponseEntity<>("Permission created successfully \n" + p, HttpStatus.CREATED);
    }

    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<String> deletePermissionsById(@PathVariable Long id)
    {
        try {
            return new ResponseEntity<>(permissionServiceImplementation.deletePermission(id), HttpStatus.OK);
        } catch (ResponseStatusException e)
        {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/permissions/{id}")
    public ResponseEntity<String> updatePermissions(@RequestBody Permission p,
                                                @PathVariable Long id)
    {
        try {
            permissionServiceImplementation.updatePermission(p, id);
            return new ResponseEntity<>("Edited manager with id: "+id+" \n" +p, HttpStatus.OK);
        } catch(ResponseStatusException e)
        {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
