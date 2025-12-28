package com.carolinawings.webapp.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank
    @Size(min = 1, max = 30)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 30)
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 40)
    @Email
    private String username;

    @NotBlank
    private String phoneNumber;

    @NotNull
    private boolean newsletterMember;

    private Set<String> roles;

    @NotBlank
    @Size(min = 8, max=50)
    private String password;
}