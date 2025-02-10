package com.message.aws.core.useCase;

import com.message.aws.application.service.VideoServiceImpl;
import com.message.aws.common.utils.JwtUtil;
import com.message.aws.core.model.domain.VideoMessagePublisher;
import com.message.aws.core.model.dto.StatusDTO;
import com.message.aws.core.model.dto.UserDTO;
import com.message.aws.core.model.dto.UserVideosDTO;
import com.message.aws.core.model.enums.VideoStatus;
import com.message.aws.core.port.AuthenticationPort;
import com.message.aws.core.port.DatabasePort;
import com.message.aws.core.port.SNSPublisherPort;
import com.message.aws.core.useCase.UploadUseCase;
import com.message.aws.infrastructure.configuration.S3Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class UploadUseCaseTest {

    private UploadUseCase uploadUseCase;
    private S3Config s3Config;
    private VideoServiceImpl videoServiceImpl;
    private SNSPublisherPort snsPublisherPort;
    private AuthenticationPort authenticationPort;
    private DatabasePort databasePort;
    private JwtUtil jwtUtil;
    private S3Client s3Client;

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
        when(s3Config.getS3Client()).thenReturn(s3Client);

        uploadUseCase = new UploadUseCase(s3Config,snsPublisherPort, databasePort);
    }



    @Test
    void testUploadFileError() throws IOException {
        MultipartFile file = new MockMultipartFile("video.mp4", "video.mp4", "video/mp4", "test video content".getBytes());
        String authorizationHeader = "Bearer invalid-token";

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setUsername("Test User");

        UserVideosDTO userVideosDTO = new UserVideosDTO();
        userVideosDTO.setId(1L);
        userVideosDTO.setUserId(1l);
        userVideosDTO.setVideoStatus(VideoStatus.IN_PROGRESS);
        userVideosDTO.setVideoKey("video.mp4");

        StatusDTO statusDTO = new StatusDTO(); // Mock the statusDTO

        when(jwtUtil.getUser(authorizationHeader)).thenReturn(userDTO);

        when(authenticationPort.validateAuthorizationHeader(authorizationHeader)).thenReturn(true);

        when(databasePort.saveOrUpdateVideo(any(UserVideosDTO.class))).thenReturn(userVideosDTO);

        when(databasePort.saveOrUpdateVideoStatus(any(StatusDTO.class))).thenReturn(statusDTO);

        when(s3Config.getS3Client().createMultipartUpload(any(CreateMultipartUploadRequest.class))).thenThrow(S3Exception.builder().message("S3 Error").build());

        assertThrows(RuntimeException.class, () -> uploadUseCase.upload(file, userDTO));

    }

    @Test
    void testUploadFile() throws IOException {
        MultipartFile file = new MockMultipartFile("video.mp4", "video.mp4", "video/mp4", "test video content".getBytes());
        String authorizationHeader = "Bearer invalid-token";

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setUsername("Test User");

        UserVideosDTO userVideosDTO = new UserVideosDTO();
        userVideosDTO.setId(1L);
        userVideosDTO.setUserId(1l);
        userVideosDTO.setVideoStatus(VideoStatus.IN_PROGRESS);
        userVideosDTO.setVideoKey("video.mp4");

        StatusDTO statusDTO = new StatusDTO(); // Mock the statusDTO

        when(jwtUtil.getUser(authorizationHeader)).thenReturn(userDTO);

        when(authenticationPort.validateAuthorizationHeader(authorizationHeader)).thenReturn(true);

        when(databasePort.saveOrUpdateVideo(any(UserVideosDTO.class))).thenReturn(userVideosDTO);

        when(databasePort.saveOrUpdateVideoStatus(any(StatusDTO.class))).thenReturn(statusDTO);

        CreateMultipartUploadResponse createResponse = CreateMultipartUploadResponse.builder()
                .uploadId("upload-id")
                .build();
        when(s3Config.getS3Client().createMultipartUpload(any(CreateMultipartUploadRequest.class))).thenReturn(createResponse);

        when(s3Config.getS3Client().uploadPart(any(UploadPartRequest.class), any(RequestBody.class))).thenReturn(UploadPartResponse.builder().eTag("etag").build());

        when(this.s3Config.getS3Client().completeMultipartUpload(any(CompleteMultipartUploadRequest.class))).thenReturn(CompleteMultipartUploadResponse.builder().build());
        doNothing().when(snsPublisherPort).publishMessage(any(VideoMessagePublisher.class));

        assertDoesNotThrow(() -> uploadUseCase.upload(file, userDTO));

    }
}
