package com.message.aws.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.message.aws.core.model.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

    public DecodedJWT decodeToken(String token) {
        String cleanToken = token.replace("Bearer ", "").trim();
        return JWT.decode(cleanToken);
    }

    public String getNameFromToken(String token) {
        DecodedJWT decodedJWT = decodeToken(token);

        return decodedJWT.getClaim("name").asString();
    }

    public String getEmailFromToken(String token) {
        DecodedJWT decodedJWT = decodeToken(token);

        return decodedJWT.getClaim("email").asString();
    }

    public UserDTO getUser(String token){
        UserDTO user = new UserDTO();

        user.setUsername(getNameFromToken(token));
        user.setEmail(getEmailFromToken(token));

        return user;
    }

    public Boolean isTokenExpired(String token) {
        try {
            DecodedJWT decodedJWT = decodeToken(token);

            if (decodedJWT.getClaim("exp").isNull()) {
                return true;
            }

            long expirationTime = decodedJWT.getClaim("exp").asLong();
            long currentTime = System.currentTimeMillis() / 1000;


            return expirationTime < currentTime;
        } catch (Exception e) {
            return true;
        }
    }
}
