package com.message.aws.application.service;

import com.message.aws.core.model.dto.UserVideosDTO;

import java.util.List;

public interface VideoService {
    List<UserVideosDTO> getVideosByUser(Long userId);
}
