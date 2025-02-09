package com.message.aws.infrastructure.security;

import com.message.aws.application.service.JwtServiceImpl;
import com.message.aws.core.model.entity.UserEntity;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @InjectMocks
    JwtAuthFilter jwtAuthFilter;

    @Mock
    JwtServiceImpl jwtServiceImpl;

    @Mock
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    MockHttpServletRequest req;

    @Mock
    MockHttpServletResponse res;

    @Mock
    MockFilterChain chain;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doFilterInternal() throws ServletException, IOException {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(Long.valueOf(1));
        userEntity.setUsername("cjafet");
        userEntity.setEmail("example@test.com");
        userEntity.setAuthorities(Collections.EMPTY_LIST);

        String token = "abcdefghijklmnopqrstuvxyz";
        String username = "cjafet";

        when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtServiceImpl.extractUsername(token)).thenReturn(username);
        when(userDetailsServiceImpl.loadUserByUsername(username)).thenReturn(userEntity);
        when(jwtServiceImpl.isTokenValid(token, userEntity, req.getRequestURI())).thenReturn(true);

        jwtAuthFilter.doFilterInternal(req, res, chain);
    }
}
