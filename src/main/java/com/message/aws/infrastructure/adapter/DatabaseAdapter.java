package com.message.aws.infrastructure.adapter;

import com.message.aws.core.model.dto.StatusDTO;
import com.message.aws.core.model.dto.UserDTO;
import com.message.aws.core.model.dto.UserVideosDTO;
import com.message.aws.core.model.entity.StatusEntity;
import com.message.aws.core.model.entity.UserEntity;
import com.message.aws.core.model.entity.VideoEntity;
import com.message.aws.core.port.DatabasePort;
import com.message.aws.core.port.repository.StatusRepository;
import com.message.aws.core.port.repository.UserRepository;
import com.message.aws.core.port.repository.VideoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatabaseAdapter implements DatabasePort {

    private final StatusRepository statusRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public DatabaseAdapter(StatusRepository statusRepository, VideoRepository videoRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.statusRepository = statusRepository;
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public StatusDTO saveOrUpdateVideoStatus(StatusDTO statusDTO) {
        StatusEntity statusEntity = modelMapper.map(statusDTO, StatusEntity.class);
        StatusEntity savedStatusEntity = statusRepository.save(statusEntity);
        return modelMapper.map(savedStatusEntity, StatusDTO.class);
    }

    @Override
    public UserVideosDTO saveOrUpdateVideo(UserVideosDTO userVideosDTO) {
        VideoEntity videoEntity = modelMapper.map(userVideosDTO, VideoEntity.class);
        VideoEntity savedVideoEntity = videoRepository.save(videoEntity);
        return modelMapper.map(savedVideoEntity, UserVideosDTO.class);
    }

    @Override
    public Optional<UserDTO> getUserByEmail(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        return userEntity.map(entity -> modelMapper.map(entity, UserDTO.class));
    }

    @Override
    public Optional<StatusDTO> getStatusByVideoId(Long videoId) {
        Optional<StatusEntity> statusEntity = statusRepository.findByVideoId(videoId);
        return statusEntity.map(entity -> modelMapper.map(entity, StatusDTO.class));
    }

    @Override
    public Optional<UserVideosDTO> getVideoById(Long videoId) {
        Optional<VideoEntity> videoEntity = videoRepository.findById(videoId);
        return videoEntity.map(entity -> modelMapper.map(entity, UserVideosDTO.class));
    }
}
