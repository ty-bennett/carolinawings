/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.UserResponse;
import com.carolinawings.webapp.dto.UserResponseDTO;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserResponse getAllUsers();
    UserResponse getAllUsersPaged(Integer page, Integer pageSize);
    Optional<UserResponseDTO> getUserById(UUID id);
    UserResponseDTO createUser(UserResponseDTO user);
    UserResponseDTO deleteUser(UUID id);
    UserResponseDTO updateUser(UserResponseDTO user, UUID id);
}
