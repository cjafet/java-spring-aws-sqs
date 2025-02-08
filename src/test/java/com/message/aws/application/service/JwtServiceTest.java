//package com.message.aws.application.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Base64;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class JwtServiceTest {
//
//    private JwtServiceImpl jwtServiceImpl;
//
//    @Mock
//    private UserDetails userDetails;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        jwtServiceImpl = new JwtServiceImpl();
//    }
//
//    @Test
//    void shouldExtractUsernameFromToken() {
//        // Criando um token de teste
//        String header = Base64.getUrlEncoder().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
//        String payload = Base64.getUrlEncoder().encodeToString("{\"name\":\"testUser\"}".getBytes());
//        String token = header + "." + payload + ".signature";
//
//        // Executando o método
//        String username = jwtServiceImpl.extractUsername(token);
//
//        // Verificando o resultado
//        assertEquals("testUser", username);
//    }
//
//    @Test
//    void shouldReturnTrueWhenTokenIsValid() {
//        // Criando um token válido
//        String header = Base64.getUrlEncoder().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
//        String payload = Base64.getUrlEncoder().encodeToString("{\"name\":\"validUser\"}".getBytes());
//        String token = header + "." + payload + ".signature";
//
//        when(userDetails.getUsername()).thenReturn("validUser");
//
//        boolean isValid = jwtServiceImpl.isTokenValid(token, userDetails, "");
//
//        assertTrue(isValid);
//    }
//
//    @Test
//    void shouldReturnFalseWhenTokenIsInvalid() {
//        // Criando um token com nome diferente
//        String header = Base64.getUrlEncoder().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
//        String payload = Base64.getUrlEncoder().encodeToString("{\"name\":\"wrongUser\"}".getBytes());
//        String token = header + "." + payload + ".signature";
//
//        when(userDetails.getUsername()).thenReturn("validUser");
//
//        boolean isValid = jwtServiceImpl.isTokenValid(token, userDetails, "");
//
//        assertFalse(isValid);
//    }
//}
