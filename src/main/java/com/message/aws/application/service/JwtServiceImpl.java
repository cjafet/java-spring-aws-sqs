package com.message.aws.application.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.message.aws.core.model.entity.UserEntity;
import com.message.aws.core.port.services.JwtService;
import com.message.aws.core.model.dto.TokenPayloadDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class JwtServiceImpl implements JwtService {

    @Override
    public String extractUsername(String token) {
        Gson gson = new GsonBuilder().create();
        TokenPayloadDTO payload = gson.fromJson(extractPayload(token), TokenPayloadDTO.class);

        return payload.getName();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails, String requestURI) {
        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername()));
    }

    @Override
    public String extractPayload(String token) {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        return new String(decoder.decode(chunks[1]));
    }

    @Override
    public Long extractUserId(UserDetails userDetails, String requestURI) {
        UserEntity userEntity = (UserEntity) userDetails;
        String[] requestPath = requestURI.split("/");
        return Long.parseLong(requestPath[requestPath.length-1]);
    }


}
