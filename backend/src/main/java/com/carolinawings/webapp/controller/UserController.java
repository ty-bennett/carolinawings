package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.config.ApplicationConstants;
import com.carolinawings.webapp.dto.UserRequestDTO;
import com.carolinawings.webapp.dto.UserResponse;
import com.carolinawings.webapp.dto.UserResponseDTO;
import com.carolinawings.webapp.service.UserServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class UserController {

    private final UserServiceImplementation userServiceImplementation;

    public UserController(UserServiceImplementation userServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
    }

    // Get a user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<Optional<UserResponseDTO>> getUserById(@PathVariable UUID id) {
        return new ResponseEntity<>(userServiceImplementation.getUserById(id), HttpStatus.OK);
    }

    // Delete a user by ID
    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> deleteUserById(@PathVariable UUID id) {
        UserResponseDTO deletedUser = userServiceImplementation.deleteUser(id);
        return new ResponseEntity<>(deletedUser, HttpStatus.OK);
    }

    // Update a user
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserRequestDTO userDTO,
                                                      @PathVariable UUID id) {
        UserResponseDTO savedUserDTO = userServiceImplementation.updateUser(userDTO, id);
        return new ResponseEntity<>(savedUserDTO, HttpStatus.OK);
    }

}
