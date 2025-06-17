/*
Ty Bennett
*/
package com.carolinawings.webapp.controller;

import com.carolinawings.webapp.model.User;
//import com.carolinawings.webapp.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

//@RestController
//@RequestMapping("/admin")
//public class UserController {
//    private UserService userServiceImplementation;
//    @Autowired
//    public UserController(UserService userServiceImpl)
//    {
//        this.userServiceImplementation = userServiceImpl;
//    }
//
//    @PostMapping("/users")
//    public ResponseEntity<String> createUser(@RequestBody User u)
//    {
//        try {
//            String status = userServiceImplementation.save(u);
//            return new ResponseEntity<>(status, HttpStatus.OK);
//        } catch (ResponseStatusException r)
//        {
//            return new ResponseEntity<>(r.getReason(), r.getStatusCode());
//        }
//    }

//    @GetMapping("/users")
//    public List<User> getAllUsers()
//    {
//        return userService.getAllUsers();
//    }
//}
