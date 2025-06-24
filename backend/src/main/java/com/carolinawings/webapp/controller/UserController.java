/*
Ty Bennett
*/
package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.model.User;
//import com.carolinawings.webapp.service.UserService;
import com.carolinawings.webapp.service.UserServiceImplementation;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class UserController {

    private final UserServiceImplementation userServiceImplementation;

    public UserController(UserServiceImplementation userServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userServiceImplementation.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable UUID id) {
        return new ResponseEntity<>(userServiceImplementation.getUserById(id), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
        userServiceImplementation.createUser(user);
        return new ResponseEntity<>("User created successfully:\n" + user, HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable UUID id) {
        return new ResponseEntity<>(userServiceImplementation.deleteUser(id), HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable UUID id,
                                             @Valid @RequestBody User user) {
        userServiceImplementation.updateUser(id, user);
        return new ResponseEntity<>("User edited with existing id: " + id, HttpStatus.OK);
    }
}