package com.message.aws.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.message.aws.security.TokenPayload;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class JwtService {

    public String extractUsername(String token) {
        Gson gson = new GsonBuilder().create();

        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        TokenPayload payload = gson.fromJson(new String(decoder.decode(chunks[1])), TokenPayload.class);

        return payload.getName();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()));
    }


}
