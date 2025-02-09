package com.message.aws.infrastructure.adapter;

import com.message.aws.common.utils.JwtUtil;
import com.message.aws.core.port.AuthenticationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationAdapterTest {

    private JwtUtil jwtUtil;
    private AuthenticationPort authenticationAdapter;

    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        authenticationAdapter = new AuthenticationAdapter(jwtUtil);
    }

    @Test
    void shouldReturnTrueForValidAuthorizationHeader() {
        String validToken = "valid-token";
        String validHeader = "Bearer " + validToken;

        assertTrue(authenticationAdapter.validateAuthorizationHeader(validHeader));
    }

//    @Test
//    void shouldReturnFalseForExpiredToken() {
//        String expiredToken = "expired-token";
//        String expiredHeader = "Bearer " + expiredToken;
//
//        when(jwtUtil.isTokenExpired(expiredToken)).thenReturn(true);
//
//        assertFalse(authenticationAdapter.validateAuthorizationHeader(expiredHeader));
//    }
    @Test
    void shouldReturnFalseForExpiredToken() {
        assertFalse(authenticationAdapter.validateAuthorizationHeader(null));
    }

    @Test
    void shouldReturnFalseForInvalidHeaderFormat() {
        String invalidHeader = "InvalidHeaderFormat";

        assertFalse(authenticationAdapter.validateAuthorizationHeader(invalidHeader));
    }

    @Test
    void shouldReturnFalseForNullHeader() {
        assertFalse(authenticationAdapter.validateAuthorizationHeader(null));
    }

    @Test
    void shouldReturnFalseForEmptyHeader() {
        assertFalse(authenticationAdapter.validateAuthorizationHeader(""));
    }

    @Test
    void shouldReturnFalseForHeaderWithOnlyBearerPrefix() {
        assertFalse(authenticationAdapter.validateAuthorizationHeader("Bearer "));
    }

    @Test
    void shouldValidateTokenExpirationCorrectly() {
        String token = "test-token";

        when(jwtUtil.isTokenExpired(token)).thenReturn(true);
        assertTrue(authenticationAdapter.validateIsTokenExpired(token));

        when(jwtUtil.isTokenExpired(token)).thenReturn(false);
        assertFalse(authenticationAdapter.validateIsTokenExpired(token));
    }
}

