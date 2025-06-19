/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface UserService {
    List<User> findAllUsers();
    Optional<User> findUserById(UUID id);
    String createUser(User user);
    String deleteUser(UUID id);
    User updateUser(UUID id, User user);
}
