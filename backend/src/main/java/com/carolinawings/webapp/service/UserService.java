/*
Written by Ty Bennett
 */

package com.carolinawings.webapp.service;

import com.carolinawings.webapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public UserService(UserRepository userRepository) {
    }

}
