/*
Ty Bennett
*/
package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.model.User;
//import com.carolinawings.webapp.service.UserService;
import com.carolinawings.webapp.service.UserServiceImplementation;
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
@RequestMapping("/admin/restaurant")
public class UserController {
    private final UserServiceImplementation userServiceImplementation;

    public UserController(UserServiceImplementation userServiceImplementation)
    {
        this.userServiceImplementation = userServiceImplementation;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userServiceImplementation.findAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable UUID id)
    {
        return new ResponseEntity<>(userServiceImplementation.findUserById(id), HttpStatus.OK);
    }



    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User u)
    {
        userServiceImplementation.createUser(u);
        return new ResponseEntity<>("User with id" + u.getId()+"created successfully \n "+ u, HttpStatus.OK);
    }


}
