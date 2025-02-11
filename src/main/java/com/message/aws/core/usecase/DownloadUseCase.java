package com.message.aws.core.usecase;

import com.message.aws.core.exception.ResourceNotFoundException;
import com.message.aws.infrastructure.configuration.S3Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
@Service
public class DownloadUseCase {


    private final S3Config s3Config;

    private String bucketZipName = "frames-video-bucket";

    public DownloadUseCase(S3Config s3Config) {
        this.s3Config = s3Config;
    }

    public Resource download(String videoKeyName,String authorizationHeader) {
        String fileName = videoKeyName.replace(".mp4", ".zip");


        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketZipName)
                .key(fileName)
                .build();
        try {
            ResponseInputStream<GetObjectResponse> getObjectResponse = s3Config.getS3Client().getObject(getObjectRequest);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getObjectResponse.readAllBytes());
            return new InputStreamResource(byteArrayInputStream);

        } catch (S3Exception | IOException e) {
            log.error("Erro ao fazer download do arquivo: {}", e.getMessage());

            throw new ResourceNotFoundException("Arquivo não encontrado");
        }
    }
}
