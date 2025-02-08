package com.message.aws.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil = new JwtUtil();


    @Test
    void testGetNameFromToken() {
        String token = JWT.create()
                .withClaim("name", "Maria Nunes")
                .sign(Algorithm.HMAC256("secret"));
        String nameFromToken = jwtUtil.getNameFromToken(token);
        assertEquals("Maria Nunes", nameFromToken, "O nome do token não é o esperado.");
    }

    @Test
    void testGetEmailFromToken() {
        String token = JWT.create()
                .withClaim("email", "maria.nunes@example.com")
                .sign(Algorithm.HMAC256("secret"));
        String emailFromToken = jwtUtil.getEmailFromToken(token);
        assertEquals("maria.nunes@example.com", emailFromToken, "O email do token não é o esperado.");
    }

    @Test
    void testIsTokenExpired() {
        String token = JWT.create()
                .withClaim("exp", System.currentTimeMillis() / 1000 + 3600)
                .sign(Algorithm.HMAC256("secret"));
        boolean isExpired = jwtUtil.isTokenExpired(token);
        assertFalse(isExpired, "O token não deveria estar expirado.");
    }

    @Test
    void testIsTokenExpiredExpired() {
        String token = JWT.create()
                .withClaim("exp", System.currentTimeMillis() / 1000 - 3600)
                .sign(Algorithm.HMAC256("secret"));
        boolean isExpired = jwtUtil.isTokenExpired(token);
        assertTrue(isExpired, "O token deveria estar expirado.");
    }

    @Test
    void testIsTokenExpiredInvalidToken() {
        String invalidToken = "invalid.token.string";
        Boolean isExpired = jwtUtil.isTokenExpired(invalidToken);
        assertTrue(isExpired, "O token inválido deve ser considerado expirado.");
    }

}