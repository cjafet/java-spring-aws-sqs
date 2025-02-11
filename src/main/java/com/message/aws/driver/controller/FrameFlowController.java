package com.message.aws.driver.controller;

import com.message.aws.core.usecase.DownloadUseCase;
import com.message.aws.core.usecase.UploadUseCase;
import com.message.aws.driver.api.FrameFlowApi;
import com.message.aws.core.model.dto.UserDTO;
import com.message.aws.core.model.dto.UserVideosDTO;
import com.message.aws.core.port.AuthenticationPort;
import com.message.aws.application.service.VideoServiceImpl;
import com.message.aws.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
public class FrameFlowController implements FrameFlowApi {


    private final DownloadUseCase downloadUseCase;

    private final VideoServiceImpl videoServiceImpl;

    private final UploadUseCase uploadUseCase;

    private final JwtUtil jwtUtil;


    public FrameFlowController(VideoServiceImpl videoServiceImpl, AuthenticationPort authenticationPort, JwtUtil jwtUtil, UploadUseCase uploadUseCase, DownloadUseCase downloadUseCase) {

        this.downloadUseCase = downloadUseCase;
        this.videoServiceImpl = videoServiceImpl;
        this.jwtUtil = jwtUtil;
        this.uploadUseCase = uploadUseCase;

    }

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile file, String authorizationHeader) throws IOException {

        UserDTO userDTO = jwtUtil.getUser(authorizationHeader);
        uploadUseCase.upload(file, userDTO);

        return ResponseEntity.ok("Upload de vídeo realizado com sucesso!");

    }


    @Override
    public ResponseEntity<Resource> downloadFile(String videoKeyName, String authorizationHeader) {

        Resource resource = downloadUseCase.download(videoKeyName, authorizationHeader);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @Override
    public ResponseEntity<List<UserVideosDTO>> listVideosByUser(Long userId) {
        return ResponseEntity.ok()
                .body(videoServiceImpl.getVideosByUser(userId));
    }


}
