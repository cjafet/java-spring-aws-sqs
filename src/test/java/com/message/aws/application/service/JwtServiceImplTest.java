package com.message.aws.application.service;

import com.message.aws.core.model.entity.UserEntity;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @InjectMocks
    private JwtServiceImpl jwtServiceImpl;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void isTokenValid() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiQ2FybG9zIiwiZXhwIjoxNzM4OTU1MTQ1LCJlbWFpbCI6Imp1bGlhbm8xMzAxOTlAaG90bWFpbC5jb20ifQ.NJVVnZ4GFUJRCSmquj-8z4B3vsX63NLePGFBottXkqA";
        UserEntity userEntity = new UserEntity();
        userEntity.setId(Long.valueOf(1));
        userEntity.setUsername("Carlos");
        userEntity.setEmail("example@test.com");
        userEntity.setAuthorities(Collections.EMPTY_LIST);

        Boolean response = jwtServiceImpl.isTokenValid(token, userEntity, "http://");

        Assert.assertEquals(response, true);
    }

    // Test covered by isTokenValid() test
    void extractUsername() {

    }

    // Test covered by isTokenValid() test
    void extractPayload() {
    }

    @Test
    void extractUserId() {
    }
}
