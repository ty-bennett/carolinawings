/*
Ty Bennett
*/
package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.UserResponseDTO;
import com.carolinawings.webapp.mapper.UserMapper;
import com.carolinawings.webapp.model.User;
import com.carolinawings.webapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public String createUser(User user) {
       userRepository.save(user);
       return "User with id " + user.getId() + " created";
    }

    @Override
    public String deleteUser(UUID id) {
        if(!userRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found");
        userRepository.deleteById(id);
        return "User with id " + id + " deleted";
    }

    @Override
    public User updateUser(UUID id, User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setPassword(user.getPassword());
                    existingUser.setPhoneNumber(user.getPhoneNumber());
                    existingUser.setNewsletterMember(user.getNewsletterMember());
                    existingUser.setEnabled(user.getEnabled());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " not found"));
    }
}
