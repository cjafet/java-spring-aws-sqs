package com.message.aws.core.port.repository;

import com.message.aws.core.model.entity.UserEntity;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Hidden
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String userName);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findById(Long id);
    UserEntity save(UserEntity userEntity);
}
