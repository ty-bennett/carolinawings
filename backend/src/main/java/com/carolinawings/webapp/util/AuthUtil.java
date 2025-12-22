package com.carolinawings.webapp.util;

import com.carolinawings.webapp.model.User;
import com.carolinawings.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class AuthUtil {

    @Autowired
    private UserRepository userRepository;

    private static final ThreadLocal<User> testUser = new ThreadLocal<>();

    public String loggedInEmail(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        return user.getUsername();
    }

    public UUID loggedInUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        return user.getId();
    }

    public User loggedInUser(){
        if(testUser.get() == null){
            return testUser.get();
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        return user;
    }

    public void setTestUser(User user) {
        testUser.set(user);
    }
}
