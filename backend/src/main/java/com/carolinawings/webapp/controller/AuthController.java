package com.carolinawings.webapp.controller;
import com.carolinawings.webapp.model.Role;
import com.carolinawings.webapp.enums.RoleName;
import com.carolinawings.webapp.model.User;
import com.carolinawings.webapp.repository.RoleRepository;
import com.carolinawings.webapp.repository.UserRepository;
import com.carolinawings.webapp.security.MessageResponse;
import com.carolinawings.webapp.security.SignUpRequest;
import com.carolinawings.webapp.security.UserInfoResponse;
import com.carolinawings.webapp.security.jwt.JwtUtils;
import com.carolinawings.webapp.security.jwt.LoginRequest;
import com.carolinawings.webapp.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest request)
    {
        Authentication auth;
        try{
            auth = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUserName(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
                toDisplayCase(userDetails.getName()), userDetails.getUsername(), jwtToken, roles, userDetails.getRestaurants());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Account already registered!" + "ERROR:" + HttpStatus.BAD_REQUEST));
        }

        User user = new User(toDisplayCase(signUpRequest.getUsername()),
                signUpRequest.getPassword(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getPhoneNumber(),
                signUpRequest.isNewsletterMember(),
                signUpRequest.getName()
        );

        Set<String> setRoles = signUpRequest.getRoles();

        Set<Role> roles = new HashSet<>();

        if(setRoles == null)
        {
            Role userRole = roleRepository.findByName(RoleName.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            roles.add(userRole);
        } else {
            setRoles.forEach(role -> {
                switch (role) {
                    case "restaurantadmin":
                        Role restaurantAdminRole = roleRepository.findByName(RoleName.RESTAURANT_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: role is not found"));
                        roles.add(restaurantAdminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleName.USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                        break;
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    private static String toDisplayCase(String s) {
        final String ACTIONABLE_DELIMITERS = " '-/"; // these cause the character following
        // to be capitalized

        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf(c) >= 0); // explicit cast not needed
        }
        return sb.toString();
    }
}
