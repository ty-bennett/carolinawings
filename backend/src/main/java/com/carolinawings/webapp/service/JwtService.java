/*
Written by Ty Bennett
*/

package com.carolinawings.webapp.service;

import lombok.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
   @Value("${sercurity.jwt.secret-key")
    private String secretKey;
   private long jwtExpirationTime;
}
