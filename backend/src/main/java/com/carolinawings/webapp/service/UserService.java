
package com.carolinawings.webapp.service;

import com.carolinawings.webapp.model.User;
import com.carolinawings.webapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
