package com.message.aws.driver.api;

import com.message.aws.core.model.entity.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/")
public interface UserApi {
    @PostMapping
    ResponseEntity<?> createUser(@RequestBody UserEntity userEntity);

    @GetMapping("/email/{email}")
    ResponseEntity<?> findByEmail(@PathVariable String email);

    @GetMapping("/username/{username}")
    ResponseEntity<?> findByUsername(@PathVariable String username);

    @PutMapping("/{id}")
    ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserEntity userEntity);
}
