package com.message.aws.application.service;

import com.message.aws.core.model.entity.UserEntity;
import com.message.aws.core.port.repository.UserRepository;
import com.message.aws.util.TestUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Test
    void createUser() {
        UserEntity userEntity = TestUtil.getUserEntity(Long.valueOf(1), "cjafet", "example@test.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        userService.createUser(userEntity);

        verify(userRepository, times(1)).save(any(UserEntity.class));

    }

    @Test
    void createUser_shoulThrowException() {
        UserEntity userEntity = TestUtil.getUserEntity(Long.valueOf(1), "cjafet", "example@test.com");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));
            userService.createUser(userEntity);
        });

        String expectedMessage = "Email já cadastrado";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void findByEmail() {
        userService.findByEmail("example@test.com");
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void findByUsername() {
        userService.findByUsername("carlos.jafet");
        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    void updateUser() {
        UserEntity userEntity = TestUtil.getUserEntity(Long.valueOf(1), "cjafet", "example@test.com");

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity updatedUser = userService.updateUser(Long.valueOf(1), "carlos.jafet", "carlos.jafet@test.com");

        Assert.assertEquals(updatedUser.getUsername(), "carlos.jafet");
        Assert.assertEquals(updatedUser.getEmail(), "carlos.jafet@test.com");
    }
}
