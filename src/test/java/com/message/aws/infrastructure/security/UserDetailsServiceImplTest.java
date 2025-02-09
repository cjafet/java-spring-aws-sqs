package com.message.aws.infrastructure.security;

import com.message.aws.core.model.entity.UserEntity;
import com.message.aws.core.port.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Mock
    UserRepository userRepository;

    @Test
    void loadUserByUsername() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(Long.valueOf(1));
        userEntity.setUsername("cjafet");
        userEntity.setEmail("example@test.com");
        userEntity.setAuthorities(Collections.EMPTY_LIST);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        userDetailsService.loadUserByUsername("cjafet");

        verify(userRepository, times(1)).findByUsername(anyString());

    }

    @Test
    void loadUserByUsername_should_throw_exception() {

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
            userDetailsService.loadUserByUsername("cjafet");
        });

        String expectedMessage = "User not authorized to download this video";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(userRepository, times(1)).findByUsername(anyString());

    }
}
