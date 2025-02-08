package com.message.aws.core.port.services;

import com.message.aws.core.model.dto.UserVideosDTO;

import java.util.List;

public interface VideoService {
    List<UserVideosDTO> getVideosByUser(Long userId);
}
