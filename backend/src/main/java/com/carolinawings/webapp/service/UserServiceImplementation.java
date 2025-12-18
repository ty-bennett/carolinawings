/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.dto.UserRequestDTO;
import com.carolinawings.webapp.dto.UserResponse;
import com.carolinawings.webapp.dto.UserResponseDTO;
import com.carolinawings.webapp.exceptions.APIException;
import com.carolinawings.webapp.exceptions.ResourceNotFoundException;
import com.carolinawings.webapp.model.User;
import com.carolinawings.webapp.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse getAllUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty())
            throw new APIException("No users present");
        List<UserResponseDTO> userDTOS = users.stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .toList();

        return new UserResponse(userDTOS);
    }

    @Override
    public UserResponse getAllUsersPaged(Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<User> users = userRepository.findAll(pageDetails);
        List<User> usersPageable = users.getContent();
        if(usersPageable.isEmpty())
            throw new APIException("No users present");
        List<UserResponseDTO> userDTOS = usersPageable.stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .toList();
        UserResponse uR = new UserResponse();
        uR.setContent(userDTOS);
        uR.setPageNumber(users.getNumber());
        uR.setPageSize(users.getSize());
        uR.setTotalElements(users.getTotalElements());
        uR.setTotalPages(users.getTotalPages());
        uR.setLastPage(users.isLast());
        return uR;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        // Example check: unique username or email (adjust based on your User entity)
        User savedUser = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new APIException("User with the email"+ user.getUsername() + " already exists"));
        User returnUser = userRepository.save(user);
        return modelMapper.map(returnUser, UserResponseDTO.class);
    }

    @Override
    public Optional<UserResponseDTO> getUserById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return user.map((element) -> modelMapper.map(element, UserResponseDTO.class));
    }

    @Override
    public UserResponseDTO deleteUser(UUID id) {
        User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "userId", id));
        userRepository.delete(u);
        return modelMapper.map(u, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO updateUser(UserRequestDTO userDTO, UUID id) {
        User savedUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId: ", id));
        User user = modelMapper.map(userDTO, User.class);
        user.setId(id);
        User savedUserToRepo = userRepository.save(user);
        return modelMapper.map(savedUserToRepo, UserResponseDTO.class);
    }

    @Override
    public boolean userManagesRestaurant(UUID id, Long restaurantId) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "userId", id));
        boolean exists = user.getRestaurants()
                .stream()
                .filter(r -> r.getId()
                        .equals(restaurantId))
                .findFirst()
                .isPresent();
        return exists;
    }
}
