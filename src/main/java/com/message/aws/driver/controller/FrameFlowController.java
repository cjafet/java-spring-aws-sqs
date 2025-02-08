package com.message.aws.driver.controller;

import com.message.aws.api.FrameFlowApi;
import com.message.aws.configuration.S3Config;
import com.message.aws.model.domain.VideoMessagePublisher;
import com.message.aws.model.dto.UserDTO;
import com.message.aws.model.dto.UserVideosDTO;
import com.message.aws.port.AuthenticationPort;
import com.message.aws.port.SNSProcessorPort;
import com.message.aws.service.impl.VideoServiceImpl;
import com.message.aws.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
public class FrameFlowController implements FrameFlowApi {

    private final S3Config s3Config;

    private final VideoServiceImpl videoServiceImpl;

    private final SNSProcessorPort snsProcessorPort;

    private final AuthenticationPort authenticationPort;

    private final JwtUtil jwtUtil;

    @Value("${s3.bucket-video-original}")
    private String bucketVideoName;

    @Value("${s3.bucket-frames}")
    private String bucketZipName;

    public FrameFlowController(S3Config s3Config, VideoServiceImpl videoServiceImpl, SNSProcessorPort snsProcessorPort, AuthenticationPort authenticationPort, JwtUtil jwtUtil) {
        this.s3Config = s3Config;
        this.videoServiceImpl = videoServiceImpl;
        this.snsProcessorPort = snsProcessorPort;
        this.authenticationPort = authenticationPort;
        this.jwtUtil = jwtUtil;

    }

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile file, String authorizationHeader) {

        if (Boolean.TRUE.equals(authenticationPort.validateAuthorizationHeader(authorizationHeader))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token não fornecido ou inválido");
        }

        UserDTO userDTO = jwtUtil.getUser(authorizationHeader);

        VideoMessagePublisher videoMessagePublisher = new VideoMessagePublisher();
        String key = file.getOriginalFilename();

        try {
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

            videoMessagePublisher.setId("1");
            videoMessagePublisher.setEmail(userDTO.getEmail());
            videoMessagePublisher.setUser(userDTO.getName());
            videoMessagePublisher.setIntervalSeconds("5");
            videoMessagePublisher.setVideoKeyS3(key);
            snsProcessorPort.publishMessage(videoMessagePublisher);

            return ResponseEntity.ok("Upload de vídeo realizado com sucesso!");

        } catch (Exception ex) {
            log.error("Erro no upload multipart: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao fazer upload do arquivo.");
        }
    }


    @Override
    public ResponseEntity<Resource> downloadFile(String videoKeyName, String authorizationHeader) {
        String fileName = videoKeyName.replace(".mp4", ".zip");

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketZipName)
                .key(fileName)
                .build();
        try {
            ResponseInputStream<GetObjectResponse> getObjectResponse = s3Config.getS3Client().getObject(getObjectRequest);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getObjectResponse.readAllBytes());
            Resource resource = new InputStreamResource(byteArrayInputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, getObjectResponse.response().contentType())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (S3Exception | IOException e) {
            log.error("Erro ao fazer download do arquivo: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo não encontrado");
        }
    }


    //Todo: implementar API nova para listagem de requisicoes de videos por usuario com autenticacao via token
    //1. Anexar token na API de listagem(parametros da requisição)
    //2. Implementar a consulta da listagem de videos + status a partir do token

    @Override
    public ResponseEntity<List<UserVideosDTO>> listVideosByUser(Long userId) {
        return ResponseEntity.ok()
                .body(videoServiceImpl.getVideosByUser(userId));
    }


}
