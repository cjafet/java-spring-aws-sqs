package com.message.aws.core.usecase;

import com.message.aws.core.exception.ResourceNotFoundException;
import com.message.aws.infrastructure.configuration.S3Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DownloadUseCaseTest {

    private DownloadUseCase downloadUseCase;
    private S3Config s3Config;

    private String bucketZipName;

    private S3Client s3Client;

    @BeforeEach
    void setup(){
        initMocks(this);
        s3Config = mock(S3Config.class);
        bucketZipName = "bucket-zip-name";
        s3Client = mock(S3Client.class);

        downloadUseCase = new DownloadUseCase(s3Config);

        when(s3Config.getS3Client()).thenReturn(s3Client);
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
        Resource response = downloadUseCase.download(videoKeyName, authorizationHeader);

        // Assert

    }

    @Test
    void testDownloadFileNotFound() {
        // Arrange
        String videoKeyName = "video.mp4";
        String authorizationHeader = "Bearer valid-token";

        when(s3Client.getObject(any(GetObjectRequest.class)))
                .thenThrow(S3Exception.builder().statusCode(404).message("Not Found").build());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                downloadUseCase.download(videoKeyName, authorizationHeader));
    }
}
