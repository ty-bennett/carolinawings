package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.config.ApplicationConstants;
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

    // Get all users
    @GetMapping("/users/all")
    public ResponseEntity<UserResponse> getUsers() {
        return new ResponseEntity<>(userServiceImplementation.getAllUsers(), HttpStatus.OK);
    }

    // Get all users paginated
    @GetMapping("/users")
    public ResponseEntity<UserResponse> getUsers(
            @RequestParam(name = "pageNumber", defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize) {
        return new ResponseEntity<>(userServiceImplementation.getAllUsersPaged(pageNumber, pageSize), HttpStatus.OK);
    }

    // Get a user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<Optional<UserResponseDTO>> getUserById(@PathVariable UUID id) {
        return new ResponseEntity<>(userServiceImplementation.getUserById(id), HttpStatus.OK);
    }

    // Create a user
    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserResponseDTO userDTO) {
        UserResponseDTO savedUserDTO = userServiceImplementation.createUser(userDTO);
        return new ResponseEntity<>(savedUserDTO, HttpStatus.CREATED);
    }

    // Delete a user by ID
    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> deleteUserById(@PathVariable UUID id) {
        UserResponseDTO deletedUser = userServiceImplementation.deleteUser(id);
        return new ResponseEntity<>(deletedUser, HttpStatus.OK);
    }

    // Update a user
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserResponseDTO userDTO,
                                                      @PathVariable UUID id) {
        UserResponseDTO savedUserDTO = userServiceImplementation.updateUser(userDTO, id);
        return new ResponseEntity<>(savedUserDTO, HttpStatus.OK);
    }
}
