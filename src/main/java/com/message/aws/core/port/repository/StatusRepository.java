package com.message.aws.core.port.repository;

import com.message.aws.core.model.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<StatusEntity, Long> {
    Optional<StatusEntity> findByVideoId(Long videoId);
}
