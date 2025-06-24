/*
Ty Bennett
*/
package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.UserResponseDTO;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.mapper.UserMapper;
import com.carolinawings.webapp.model.User;
import com.carolinawings.webapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    public UserServiceImplementation(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers()
    {
        List<User> users = userRepository.findAll();
        if(users.isEmpty())
            throw new APIException("No users present");
        return users;
    }

    @Override
    public String createUser(User user) {
        User savedUser = userRepository.findByName(user.getName());
        if(savedUser != null)
            throw new APIException("User with the name " + user.getName() + " already exists");
        userRepository.save(user);
        return "User with id " + user.getId() + " added successfully";
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public String deleteUser(UUID id) {
        User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "userId", id));
        userRepository.delete(u);
        return "User with id " + id + " deleted successfully";
    }

    @Override
    public User updateUser(@PathVariable UUID id, @RequestBody User user) {
        User savedUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "userId", id));
        user.setId(id);
        return userRepository.save(savedUser);
    }
}
