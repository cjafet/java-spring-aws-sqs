package com.message.aws.driver.controller;


import com.message.aws.application.service.UserServiceImpl;
import com.message.aws.core.model.entity.UserEntity;
import com.message.aws.driver.api.UserApi;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserController implements UserApi {
    private final UserServiceImpl userServiceImpl;

    public UserController (UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }
        @Override
    public ResponseEntity<?> createUser(@RequestBody UserEntity userEntity) {
        try {
            UserEntity createdUserEntity = userServiceImpl.createUser(userEntity);
            return ResponseEntity.ok(createdUserEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        Optional<UserEntity> user = userServiceImpl.findByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<?> findByUsername(@PathVariable String username) {
        Optional<UserEntity> user = userServiceImpl.findByUsername(username);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserEntity userEntity) {
        try {
            UserEntity updatedUserEntity = userServiceImpl.updateUser(id, userEntity.getUsername(), userEntity.getEmail());
            return ResponseEntity.ok(updatedUserEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
