package com.message.aws.driver.controller;

import com.message.aws.core.model.dto.UserDTO;
import com.message.aws.core.model.dto.UserVideosDTO;
import com.message.aws.core.model.enums.VideoStatus;
import com.message.aws.core.port.AuthenticationPort;
import com.message.aws.core.port.SNSProcessorPort;
import com.message.aws.application.service.VideoServiceImpl;
import com.message.aws.common.utils.JwtUtil;
import com.message.aws.infrastructure.configuration.S3Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.ResponseInputStream;
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

class FrameFlowControllerTest {

    private FrameFlowController frameFlowController;
    private S3Config s3Config;
    private VideoServiceImpl videoServiceImpl;
    private SNSProcessorPort snsProcessorPort;
    private AuthenticationPort authenticationPort;
    private JwtUtil jwtUtil;
    private S3Client s3Client;

    @BeforeEach
    void setUp() {
        s3Config = mock(S3Config.class);
        videoServiceImpl = mock(VideoServiceImpl.class);
        snsProcessorPort = mock(SNSProcessorPort.class);
        authenticationPort = mock(AuthenticationPort.class);
        jwtUtil = mock(JwtUtil.class);
        s3Client = mock(S3Client.class);

        when(s3Config.getS3Client()).thenReturn(s3Client);

        frameFlowController = new FrameFlowController(s3Config, videoServiceImpl, snsProcessorPort, authenticationPort, jwtUtil);
    }

    @Test
    void testUploadFileUnauthorized() {
        MultipartFile file = new MockMultipartFile("video.mp4", "video.mp4", "video/mp4", "test video content".getBytes());
        String authorizationHeader = "Bearer invalid-token";

        when(authenticationPort.validateAuthorizationHeader(authorizationHeader)).thenReturn(true);

        ResponseEntity<String> response = frameFlowController.uploadFile(file, authorizationHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token não fornecido ou inválido", response.getBody());
    }

    @Test
    void testUploadFileInternalServerError() throws IOException {
        // Arrange
        MultipartFile file = new MockMultipartFile("video.mp4", "video.mp4", "video/mp4", "test video content".getBytes());
        String authorizationHeader = "Bearer valid-token";
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setUsername("Test User");

        when(authenticationPort.validateAuthorizationHeader(authorizationHeader)).thenReturn(false);
        when(jwtUtil.getUser(authorizationHeader)).thenReturn(userDTO);
        when(s3Client.createMultipartUpload(any(CreateMultipartUploadRequest.class)))
                .thenThrow(S3Exception.builder().message("S3 Error").build());

        // Act
        ResponseEntity<String> response = frameFlowController.uploadFile(file, authorizationHeader);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro ao fazer upload do arquivo.", response.getBody());
    }
    @Test
    void testDownloadFileSuccess() throws IOException {
        // Arrange
        String videoKeyName = "video.mp4";
        String authorizationHeader = "Bearer valid-token";
        String fileName = videoKeyName.replace(".mp4", ".zip");
        byte[] fileContent = "test zip content".getBytes();

        GetObjectResponse getObjectResponse = GetObjectResponse.builder()
                .contentType("application/zip")
                .build();
        when(s3Client.getObject(any(GetObjectRequest.class)))
                .thenReturn(new ResponseInputStream<>(getObjectResponse, new ByteArrayInputStream(fileContent)));

        // Act
        ResponseEntity<Resource> response = frameFlowController.downloadFile(videoKeyName, authorizationHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileContent.length, response.getBody().contentLength());
        assertEquals("application/zip", response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
        assertEquals("attachment; filename=\"" + fileName + "\"", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
    }

    @Test
    void testDownloadFileNotFound() {
        // Arrange
        String videoKeyName = "video.mp4";
        String authorizationHeader = "Bearer valid-token";

        when(s3Client.getObject(any(GetObjectRequest.class)))
                .thenThrow(S3Exception.builder().statusCode(404).message("Not Found").build());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                frameFlowController.downloadFile(videoKeyName, authorizationHeader));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Arquivo não encontrado", exception.getReason());
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
        assertEquals("http://example.com/video1.mp4", response.getBody().get(0).getVideoUrl());
        assertEquals("http://example.com/video2.mp4", response.getBody().get(1).getVideoUrl());
    }
}