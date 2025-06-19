/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.Manager;
import com.carolinawings.webapp.repository.ManagerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerServiceImplementation implements ManagerService {
    private ManagerRepository managerRepository;

    public ManagerServiceImplementation(final ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public Optional<Manager> getManagerById(Long id) {
        return managerRepository.findById(id);
    }

    @Override
    public String createManager(Manager manager) {
        managerRepository.save(manager);
        return "Manager with id: "+manager.getManagerId()+" created";
    }

    @Override
    public String deleteManagerById(Long id) {
        if(!managerRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager with id: "+id+" does not exist");
        }
        managerRepository.deleteById(id);
        return "Manager with id: "+id+" deleted";
    }

    @Override
    public Manager updateManager(Manager manager, Long id) {
       return managerRepository.findById(id)
               .map(existingManager -> {
                   existingManager.setName(manager.getName());
                   existingManager.setEmail(manager.getEmail());
                   existingManager.setPhoneNumber(manager.getPhoneNumber());
                   return managerRepository.save(existingManager);
               })
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found"));
    }
}
