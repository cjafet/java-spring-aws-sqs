package com.message.aws.application.service;

import com.message.aws.core.model.dto.UserVideosDTO;
import com.message.aws.core.model.entity.VideoEntity;
import com.message.aws.core.port.repository.VideoRepository;
import com.message.aws.core.port.services.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<UserVideosDTO> getVideosByUser(Long userId) {
        List<UserVideosDTO> userVideosDTOS = Collections.emptyList();
        try {
            List<VideoEntity> videos =  videoRepository.findAllByUserId(userId);
            userVideosDTOS = videos.stream()
                    .map(this::convertToDto)
                    .toList();
        } catch (Exception exception) {
            log.error("Erro ao listar vídeos do usuario {}", keyValue("exception", exception.getMessage()));
        }

        return userVideosDTOS;
    }

    private UserVideosDTO convertToDto(VideoEntity video) {
        return modelMapper.map(video, UserVideosDTO.class);
    }
}
