/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.Manager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ManagerService {
    List<Manager> getAllManagers();
    Optional<Manager> getManagerById(Long id);
    String createManager(Manager manager);
    String deleteManagerById(Long id);
    Manager updateManager(Manager manager, Long id);
}
