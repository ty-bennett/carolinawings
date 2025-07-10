package com.carolinawings.webapp.security;

import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoResponse {
    private UUID id;
    private String username;
    private String jwtToken;
    private List<String> roles;
}
