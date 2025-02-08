package com.message.aws.infrastructure.adapter;

import com.message.aws.core.port.AuthenticationPort;
import com.message.aws.common.utils.JwtUtil;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationAdapter implements AuthenticationPort {
    private final JwtUtil jwtUtil;
    String bearerPrefix = "Bearer ";

    public AuthenticationAdapter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean validateAuthorizationHeader(String authorizationHeader) {
        try {
            return authorizationHeader != null && !authorizationHeader.trim().isEmpty() &&
                    authorizationHeader.regionMatches(true, 0, bearerPrefix, 0, bearerPrefix.length()) &&
                    authorizationHeader.length() > bearerPrefix.length();
        } catch (Exception ex) {
            return false;
        }
    }


    @Override
    public Boolean validateIsTokenExpired(String authorizationHeader) {
        return jwtUtil.isTokenExpired(authorizationHeader);
    }
}

