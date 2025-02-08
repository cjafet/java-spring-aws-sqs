package com.message.aws.core.port.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    public String extractUsername(String token) ;
    public boolean isTokenValid(String token, UserDetails userDetails, String requestURI) ;
    public String extractPayload(String token) ;
    public Long extractUserId(UserDetails userDetails, String requestURI) ;

}
