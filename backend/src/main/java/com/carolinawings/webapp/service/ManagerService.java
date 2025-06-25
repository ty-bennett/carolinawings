/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import org.springframework.stereotype.Service;
import com.carolinawings.webapp.dto.ManagerDTO;
import com.carolinawings.webapp.dto.ManagerResponse;


import java.util.Optional;

@Service
public interface ManagerService {
    ManagerResponse getAllManagers();
    ManagerResponse getAllManagersPage(int pageNumber, int pageSize);
    ManagerDTO createManager(ManagerDTO managerDTO);
    Optional<ManagerDTO> getManagerById(Long id);
    ManagerDTO deleteManager(Long id);
    ManagerDTO updateManager(ManagerDTO managerDTO, Long id);
}