package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.model.Manager;
import com.carolinawings.webapp.service.ManagerServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class ManagerController {
    private final ManagerServiceImplementation managerServiceImplementation;

    public ManagerController(ManagerServiceImplementation managerServiceImplementation)
    {
        this.managerServiceImplementation = managerServiceImplementation;
    }

    @GetMapping("/managers")
    public ResponseEntity<List<Manager>> getManagers()
    {
       return new ResponseEntity<>(managerServiceImplementation.getAllManagers(), HttpStatus.OK);
    }

    @GetMapping("managers/{id}")
    public ResponseEntity<Optional<Manager>> getManagerById(@PathVariable Long id)
    {
        return new ResponseEntity<>(managerServiceImplementation.getManagerById(id), HttpStatus.OK);
    }

    @PostMapping("/managers")
    public ResponseEntity<String> createManager(@RequestBody Manager m)
    {
        managerServiceImplementation.createManager(m);
        return new ResponseEntity<>("Manager created successfully \n" + m, HttpStatus.CREATED);
    }

    @DeleteMapping("/managers/{id}")
    public ResponseEntity<String> deleteManagerById(@PathVariable Long id)
    {
        try {
            return new ResponseEntity<>(managerServiceImplementation.deleteManagerById(id), HttpStatus.OK);
        } catch (ResponseStatusException e)
        {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/managers/{id}")
    public ResponseEntity<String> updateManager(@RequestBody Manager m,
                                                @PathVariable Long id)
    {
        try {
            managerServiceImplementation.updateManager(m, id);
            return new ResponseEntity<>("Edited manager with id: "+id+" \n" +m, HttpStatus.OK);
        } catch(ResponseStatusException e)
        {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
