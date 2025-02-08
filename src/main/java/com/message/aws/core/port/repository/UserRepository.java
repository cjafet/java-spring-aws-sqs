package com.message.aws.core.port.repository;

import com.message.aws.core.model.entity.User;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Hidden
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String userName);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    User save(User user);
}
