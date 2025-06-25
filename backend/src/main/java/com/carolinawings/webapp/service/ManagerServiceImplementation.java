/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.ManagerDTO;
import com.carolinawings.webapp.dto.ManagerResponse;
import com.carolinawings.webapp.model.Manager;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.repository.ManagerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerServiceImplementation implements ManagerService {

    private final ManagerRepository managerRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ManagerServiceImplementation(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    public ManagerResponse getAllManagers() {
        List<Manager> managers = managerRepository.findAll();
        if (managers.isEmpty()) {
            throw new APIException("No managers present");
        }

        List<ManagerDTO> dtos = managers.stream()
                .map(manager -> modelMapper.map(manager, ManagerDTO.class))
                .toList();

        return new ManagerResponse(dtos);
    }

    @Override
    public ManagerResponse getAllManagersPage(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Manager> page = managerRepository.findAll(pageable);
        List<ManagerDTO> dtos = page.getContent()
                .stream()
                .map(manager -> modelMapper.map(manager, ManagerDTO.class))
                .toList();

        return new ManagerResponse(dtos, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages(), page.isLast());
    }

    @Override
    public ManagerDTO createManager(ManagerDTO managerDTO) {
        Manager manager = modelMapper.map(managerDTO, Manager.class);
        Manager existing = managerRepository.findByName(manager.getName());
        if (existing != null) {
            throw new APIException("Manager with email " + manager.getEmail() + " already exists");
        }
        Manager saved = managerRepository.save(manager);
        return modelMapper.map(saved, ManagerDTO.class);
    }

    @Override
    public Optional<ManagerDTO> getManagerById(Long id) {
        Optional<Manager> manager = managerRepository.findById(id);
        return manager.map(m -> modelMapper.map(m, ManagerDTO.class));
    }

    @Override
    public ManagerDTO deleteManager(Long id) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Manager", "id", id));
        managerRepository.delete(manager);
        return modelMapper.map(manager, ManagerDTO.class);
    }

    @Override
    public ManagerDTO updateManager(ManagerDTO managerDTO, Long id) {
        Manager existing = managerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Manager", "id", id));
        Manager updated = modelMapper.map(managerDTO, Manager.class);
        updated.setManagerId(id);
        Manager saved = managerRepository.save(updated);
        return modelMapper.map(saved, ManagerDTO.class);
    }
}
