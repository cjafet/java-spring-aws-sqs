package com.message.aws.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();
    }

    @Test
    void shouldExtractUsernameFromToken() {
        // Criando um token de teste
        String header = Base64.getUrlEncoder().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
        String payload = Base64.getUrlEncoder().encodeToString("{\"name\":\"testUser\"}".getBytes());
        String token = header + "." + payload + ".signature";

        // Executando o método
        String username = jwtService.extractUsername(token);

        // Verificando o resultado
        assertEquals("testUser", username);
    }

    @Test
    void shouldReturnTrueWhenTokenIsValid() {
        // Criando um token válido
        String header = Base64.getUrlEncoder().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
        String payload = Base64.getUrlEncoder().encodeToString("{\"name\":\"validUser\"}".getBytes());
        String token = header + "." + payload + ".signature";

        when(userDetails.getUsername()).thenReturn("validUser");

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void shouldReturnFalseWhenTokenIsInvalid() {
        // Criando um token com nome diferente
        String header = Base64.getUrlEncoder().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
        String payload = Base64.getUrlEncoder().encodeToString("{\"name\":\"wrongUser\"}".getBytes());
        String token = header + "." + payload + ".signature";

        when(userDetails.getUsername()).thenReturn("validUser");

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertFalse(isValid);
    }
}
