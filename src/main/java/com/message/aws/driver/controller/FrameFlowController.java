package com.message.aws.driver.controller;

import com.message.aws.api.FrameFlowApi;
import com.message.aws.model.dto.UserVideosDTO;
import com.message.aws.service.impl.VideoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
public class FrameFlowController implements FrameFlowApi {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private VideoServiceImpl videoServiceImpl;

    @Value("${s3.bucket-video-original}")
    private String BUCKET_NAME;

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile file) {

        //TODO:
        //1. Anexar token a API de upload
        //2. Criar uma requisição para o usuario e salvar no banco
        //3. Upload de video original - FEITO
        //4. Enviar requisição via SNS

       try {
           s3Client.putObject(request ->
                           request
                                   .bucket(BUCKET_NAME)
                                   .key(file.getOriginalFilename()),
                   RequestBody.fromBytes(file.getBytes()));

       }catch (Exception ex){
            log.error("Erro ao fazer upload do arquivo: {}", ex.getMessage());
            return ResponseEntity.status(500).body("feito upload do arquivo com sucesso");
       }
        return ResponseEntity.ok("feito upload do arquivo com sucesso");
    }

    @Override
    public ResponseEntity<Resource> downloadFile() {
        String fileName = "Batata-frita.jpg";

        //TODO
        //1. Anexar token e id de requisicao na API de download(parametros da requisição)
        //2. Implementar a consulta do nome do arquivo zip com os frames a partir do id da requisição passada
        //3. Download de frames do video original - FEITO


        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .build();
        try {
            ResponseInputStream<GetObjectResponse> getObjectResponse = s3Client.getObject(getObjectRequest);

            ByteArrayInputStream bais = new ByteArrayInputStream(getObjectResponse.readAllBytes());
            Resource resource = new InputStreamResource(bais);

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
