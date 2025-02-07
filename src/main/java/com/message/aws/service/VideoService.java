package com.message.aws.service;

import com.message.aws.model.dto.UserVideosDTO;

import java.util.List;

public interface VideoService {
    List<UserVideosDTO> getVideosByUser(Long userId);
}
