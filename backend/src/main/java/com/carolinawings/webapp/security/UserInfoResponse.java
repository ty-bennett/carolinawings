package com.carolinawings.webapp.security;

import com.carolinawings.webapp.model.Restaurant;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoResponse {
    private UUID id;
    private String name;
    private String username;
    private String jwtToken;
    private List<String> roles;
    private Set<Long> restaurants;
}
