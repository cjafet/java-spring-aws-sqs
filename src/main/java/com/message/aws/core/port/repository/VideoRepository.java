package com.message.aws.core.port.repository;

import com.message.aws.core.model.entity.VideoEntity;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Hidden
public interface VideoRepository extends JpaRepository<VideoEntity, Long> {

    List<VideoEntity> findAllByUserId(Long userId);
}
