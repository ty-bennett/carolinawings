/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.Manager;
import com.carolinawings.webapp.repository.ManagerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerServiceImplementation implements ManagerService {

    private final ManagerRepository managerRepository;

    public ManagerServiceImplementation(ManagerRepository managerRepository)
    {
        this.managerRepository = managerRepository;
    }

    @Override
    public List<Manager> getAllManagers()
    {
        List<Manager> managers = managerRepository.findAll();
        if(managers.isEmpty())
            throw new APIException("No managers present");
        return managers;
    }

    @Override
    public String createManager(Manager manager) {
        Manager savedManager = managerRepository.findByName(manager.getName());
        if(savedManager != null)
            throw new APIException("Manager with the name " + manager.getName() + " already exists");
        managerRepository.save(manager);
        return "Manager with id " + manager.getManagerId() + " added successfully";
    }

    @Override
    public Optional<Manager> getManagerById(Long id) {
        return managerRepository.findById(id);
    }

    @Override
    public String deleteManagerById(Long id) {
        Manager m = managerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Manager", "managerId", id));
        managerRepository.delete(m);
        return "Manager with id " + id + " deleted successfully";
    }

    @Override
    public Manager updateManager(Manager manager, Long id) {
        Manager savedManager = managerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Manager", "managerId", id));
        manager.setManagerId(id);
        return managerRepository.save(savedManager);
    }
}
