package com.message.aws.core.useCase;

import com.message.aws.core.model.domain.VideoMessagePublisher;
import com.message.aws.core.model.dto.StatusDTO;
import com.message.aws.core.model.dto.UserDTO;
import com.message.aws.core.model.dto.UserVideosDTO;
import com.message.aws.core.model.enums.VideoStatus;
import com.message.aws.core.port.DatabasePort;
import com.message.aws.core.port.SNSPublisherPort;
import com.message.aws.infrastructure.configuration.S3Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class UploadUseCase {

    private final S3Config s3Config;

    private final SNSPublisherPort snsPublisherPort;

    private final DatabasePort databasePort;

    @Value("${s3.bucket-video-original}")
    private String bucketVideoName;


    public UploadUseCase(S3Config s3Config, SNSPublisherPort snsPublisherPort, DatabasePort databasePort) {
        this.s3Config = s3Config;
        this.snsPublisherPort = snsPublisherPort;
        this.databasePort = databasePort;
    }


    public void upload(MultipartFile file, UserDTO userDTO) throws IOException {
        VideoMessagePublisher videoMessagePublisher = new VideoMessagePublisher();
        String key = file.getOriginalFilename();

        UserVideosDTO userVideosDTO = new UserVideosDTO(null, VideoStatus.PENDING, key, userDTO.getId());
        UserVideosDTO savedUserVideo = databasePort.saveOrUpdateVideo(userVideosDTO);

        StatusDTO statusDTO = new StatusDTO(null, VideoStatus.PENDING, key, Instant.now().toString(), null, userDTO.getId(), savedUserVideo.getId());
        StatusDTO savedStatusDTO = databasePort.saveOrUpdateVideoStatus(statusDTO);


        CreateMultipartUploadRequest createRequest = CreateMultipartUploadRequest.builder()
                .bucket(bucketVideoName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        CreateMultipartUploadResponse createResponse = s3Config.getS3Client().createMultipartUpload(createRequest);
        String uploadId = createResponse.uploadId();

        List<CompletedPart> completedParts = new ArrayList<>();
        byte[] fileBytes = file.getBytes();
        int partSize = 5 * 1024 * 1024;
        int partNumber = 1;

        for (int i = 0; i < fileBytes.length; i += partSize) {
            int size = Math.min(partSize, fileBytes.length - i);
            byte[] part = Arrays.copyOfRange(fileBytes, i, i + size);

            UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
                    .bucket(bucketVideoName)
                    .key(key)
                    .uploadId(uploadId)
                    .partNumber(partNumber)
                    .build();

            UploadPartResponse uploadPartResponse = s3Config.getS3Client().uploadPart(uploadPartRequest, RequestBody.fromBytes(part));
            completedParts.add(CompletedPart.builder()
                    .partNumber(partNumber)
                    .eTag(uploadPartResponse.eTag())
                    .build());

            partNumber++;
        }

        CompleteMultipartUploadRequest completeRequest = CompleteMultipartUploadRequest.builder()
                .bucket(bucketVideoName)
                .key(key)
                .uploadId(uploadId)
                .multipartUpload(mpu -> mpu.parts(completedParts))
                .build();

        s3Config.getS3Client().completeMultipartUpload(completeRequest);

        savedStatusDTO.setStatus(VideoStatus.IN_PROGRESS);
        savedStatusDTO.setModifiedDate(Instant.now().toString());
        databasePort.saveOrUpdateVideoStatus(savedStatusDTO);

        videoMessagePublisher.setId(savedUserVideo.getId().toString());
        videoMessagePublisher.setEmail(userDTO.getEmail());
        videoMessagePublisher.setUser(userDTO.getUsername());
        videoMessagePublisher.setIntervalSeconds("5");
        videoMessagePublisher.setVideoKeyS3(key);
        snsPublisherPort.publishMessage(videoMessagePublisher);



    }

}
