package com.message.aws.driver.controller;


import com.message.aws.application.service.UserService;
import com.message.aws.core.model.entity.User;
import com.message.aws.driver.api.UserApi;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserController implements UserApi {
    private final UserService userService;

    public UserController (UserService userService) {
        this.userService = userService;
    }
        @Override
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<?> findByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user.getUsername(), user.getEmail());
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
