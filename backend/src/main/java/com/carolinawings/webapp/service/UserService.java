
package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.UserResponseDTO;
import com.carolinawings.webapp.mapper.UserMapper;
import com.carolinawings.webapp.model.User;
import com.carolinawings.webapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public List<UserResponseDTO> getUsers()
    {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::toDTO).toList();
    }

    public User save(User user)
    {
        return userRepository.save(user);
    }

    public Optional<User> findById(UUID id)
    {
        return userRepository.findById(id);
    }

}
