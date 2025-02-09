package com.message.aws.core.port.services;

import com.message.aws.application.service.VideoServiceImpl;
import com.message.aws.core.model.dto.UserVideosDTO;
import com.message.aws.core.model.entity.VideoEntity;
import com.message.aws.core.port.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceImplTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private VideoServiceImpl videoService;

    private VideoEntity videoEntity;
    private UserVideosDTO userVideosDTO;

    @BeforeEach
    void setUp() {
        videoEntity = new VideoEntity();
        videoEntity.setId(1L);
        videoEntity.setUserId(100L);
        videoEntity.setVideoKey("Teste de Vídeo");

        userVideosDTO = new UserVideosDTO();
        userVideosDTO.setId(1L);
        userVideosDTO.setVideoKey("Teste de Vídeo");
        videoEntity.setVideoKey("Teste_de_Vídeo");

        userVideosDTO = new UserVideosDTO();
        userVideosDTO.setId(1L);
        userVideosDTO.setVideoKey("Teste_de_Vídeo");
    }

//    @Test
//    void testGetVideosByUser_ReturnsListOfVideos() {
//        Long userId = 100L;
//        List<VideoEntity> videoEntities = Arrays.asList(videoEntity);
//
//        when(videoRepository.findAllByUserId(userId)).thenReturn(videoEntities);
//        when(modelMapper.map(videoEntity, UserVideosDTO.class)).thenReturn(userVideosDTO);
//
//        List<UserVideosDTO> result = videoService.getVideosByUser(userId);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("Teste de Vídeo", result.get(0).getVideoKey());
//        assertEquals("Teste_de_Vídeo", result.get(0).getVideoKey());
//
//        verify(videoRepository, times(1)).findAllByUserId(userId);
//        verify(modelMapper, times(1)).map(videoEntity, UserVideosDTO.class);
//    }

    @Test
    void testGetVideosByUser_ReturnsEmptyListWhenNoVideos() {
        Long userId = 200L;
        when(videoRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

        List<UserVideosDTO> result = videoService.getVideosByUser(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(videoRepository, times(1)).findAllByUserId(userId);
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    void testGetVideosByUser_ExceptionHandledAndReturnsEmptyList() {
        Long userId = 300L;
        when(videoRepository.findAllByUserId(userId)).thenThrow(new RuntimeException("Erro de banco"));

        List<UserVideosDTO> result = videoService.getVideosByUser(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(videoRepository, times(1)).findAllByUserId(userId);
        verify(modelMapper, never()).map(any(), any());
    }
}
