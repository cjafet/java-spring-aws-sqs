package com.message.aws.common.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RequestUnauthorizedExceptionTest {

    private RequestUnauthorizedException requestUnauthorizedException;

    @BeforeEach
    void setUp() {
        requestUnauthorizedException = new RequestUnauthorizedException();
    }

    @Test
    void testHandleUnauthorizedExceptionWithReason() {
        String reason = "Token inválido";
        ResponseStatusException ex = Mockito.mock(ResponseStatusException.class);
        when(ex.getReason()).thenReturn(reason);

        Map<String, Object> response = requestUnauthorizedException.handleUnauthorizedException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.get("statusCode: "));
        assertEquals(reason, response.get("erro: "));
        assertEquals(LocalDateTime.now().toString().substring(0, 19), ((String) response.get("timestamp: ")).substring(0, 19));
    }

    @Test
    void testHandleUnauthorizedExceptionWithoutReason() {
        ResponseStatusException ex = Mockito.mock(ResponseStatusException.class);
        when(ex.getReason()).thenReturn(null);

        Map<String, Object> response = requestUnauthorizedException.handleUnauthorizedException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.get("statusCode: "));
        assertEquals("Token não fornecido ou inválido.", response.get("erro: "));
        assertEquals(LocalDateTime.now().toString().substring(0, 19), ((String) response.get("timestamp: ")).substring(0, 19));
    }
}