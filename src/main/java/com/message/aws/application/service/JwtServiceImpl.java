package com.message.aws.application.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.message.aws.core.model.entity.User;
import com.message.aws.core.port.services.JwtService;
import com.message.aws.infrastructure.security.TokenPayload;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class JwtServiceImpl implements JwtService {

    @Override
    public String extractUsername(String token) {
        Gson gson = new GsonBuilder().create();
        TokenPayload payload = gson.fromJson(extractPayload(token), TokenPayload.class);

        return payload.getName();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails, String requestURI) {
        final String username = extractUsername(token);
        final Long userId = extractUserId(userDetails, requestURI);

        return (username.equals(userDetails.getUsername()) && userId.equals(userId));
    }

    @Override
    public String extractPayload(String token) {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        return new String(decoder.decode(chunks[1]));
    }

    @Override
    public Long extractUserId(UserDetails userDetails, String requestURI) {
        User user = (User) userDetails;
        String[] requestPath = requestURI.split("/");
        return Long.parseLong(requestPath[requestPath.length-1]);
    }


}
