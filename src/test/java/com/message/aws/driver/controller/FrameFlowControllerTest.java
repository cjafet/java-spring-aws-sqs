package com.message.aws.driver.controller;

import com.message.aws.core.model.domain.VideoMessagePublisher;
import com.message.aws.core.model.dto.StatusDTO;
import com.message.aws.core.model.dto.UserDTO;
import com.message.aws.core.model.dto.UserVideosDTO;
import com.message.aws.core.model.enums.VideoStatus;
import com.message.aws.core.port.AuthenticationPort;
import com.message.aws.core.port.SNSPublisherPort;
import com.message.aws.application.service.VideoServiceImpl;
import com.message.aws.common.utils.JwtUtil;
import com.message.aws.core.port.DatabasePort;
import com.message.aws.core.useCase.DownloadUseCase;
import com.message.aws.core.useCase.UploadUseCase;
import com.message.aws.infrastructure.configuration.S3Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class FrameFlowControllerTest {

    private FrameFlowController frameFlowController;
    private S3Config s3Config;
    private VideoServiceImpl videoServiceImpl;
    private SNSPublisherPort snsPublisherPort;
    private AuthenticationPort authenticationPort;
    private DatabasePort databasePort;
    private JwtUtil jwtUtil;
    private S3Client s3Client;
    private UploadUseCase uploadUseCase;
    private DownloadUseCase downloadUseCase;

    @BeforeEach
    void setUp() {
        initMocks(this);
        s3Config = mock(S3Config.class);
        videoServiceImpl = mock(VideoServiceImpl.class);
        snsPublisherPort = mock(SNSPublisherPort.class);
        authenticationPort = mock(AuthenticationPort.class);
        databasePort = mock(DatabasePort.class);
        jwtUtil = mock(JwtUtil.class);
        s3Client = mock(S3Client.class);
        uploadUseCase = mock(UploadUseCase.class);
        downloadUseCase = mock(DownloadUseCase.class);

        when(s3Config.getS3Client()).thenReturn(s3Client);

        frameFlowController = new FrameFlowController(videoServiceImpl, authenticationPort, jwtUtil, uploadUseCase, downloadUseCase);
    }




    @Test
    void testUploadFile() throws IOException {
        MultipartFile file = new MockMultipartFile("video.mp4", "video.mp4", "video/mp4", "test video content".getBytes());
        String authorizationHeader = "Bearer invalid-token";

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setUsername("Test User");

        doNothing().when(uploadUseCase).upload(file, userDTO);

        ResponseEntity<String> response = frameFlowController.uploadFile(file, authorizationHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Upload de vídeo realizado com sucesso!", response.getBody());
    }


    @Test
    void testDownloadFileSuccess() throws IOException {
        // Arrange
        String videoKeyName = "video.mp4";
        String authorizationHeader = "Bearer valid-token";
        String fileName = videoKeyName.replace(".mp4", ".zip");
        byte[] fileContent = "test zip content".getBytes();

        when(downloadUseCase.download(videoKeyName, authorizationHeader)).thenReturn(new InputStreamResource(new ByteArrayInputStream(fileContent)));

        // Act
        ResponseEntity<Resource> response = frameFlowController.downloadFile(videoKeyName, authorizationHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileContent.length, response.getBody().contentLength());
        assertEquals("application/octet-stream", response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
    }


    @Test
    void testListVideosByUserSuccess() {
        // Arrange
        Long userId = 1L;
        List<UserVideosDTO> videos = new ArrayList<>();
        videos.add(new UserVideosDTO(1L, VideoStatus.IN_PROGRESS, "http://example.com/video1.mp4",1L));
        videos.add(new UserVideosDTO(1L, VideoStatus.IN_PROGRESS, "http://example.com/video2.mp4",1L));

        when(videoServiceImpl.getVideosByUser(userId)).thenReturn(videos);

        // Act
        ResponseEntity<List<UserVideosDTO>> response = frameFlowController.listVideosByUser(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("http://example.com/video1.mp4", response.getBody().get(0).getVideoKey());
        assertEquals("http://example.com/video2.mp4", response.getBody().get(1).getVideoKey());
    }
}
