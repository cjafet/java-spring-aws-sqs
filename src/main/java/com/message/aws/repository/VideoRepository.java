package com.message.aws.repository;

import com.message.aws.model.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<VideoEntity, Long> {

    List<VideoEntity> findAllByUserId(Long userId);
}
