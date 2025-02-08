package com.message.aws.driver.api;

import com.message.aws.core.model.dto.UserVideosDTO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
public interface FrameFlowApi {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "poc-video-original")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feito upload do arquivo com sucesso",
                    content = {@Content(mediaType = "Multipart/form-data",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "401", description = "Token não fornecido ou inválido",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao fazer upload do arquivo",
                    content = @Content)})

    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file,
                                             @RequestHeader("Authorization") String authorizationHeader);

    @GetMapping(produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Baixa arquivo zip com os frames do video original")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feito upload do arquivo com sucesso",
                    content = {@Content(mediaType = "Multipart/form-data",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao fazer upload do arquivo",
                    content = @Content)})

    public ResponseEntity<Resource> downloadFile(@Parameter(description = "Id do video") @RequestParam(required = true) String videoKeyName,
                                                 @RequestHeader("Authorization") String authorizationHeader);


    @GetMapping("videos/user/{id}")
    @Operation(summary = "list viedos by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Videos de usuário listados com sucesso",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Erro ao listar vídeos de usuário",
                    content = @Content)})

    public ResponseEntity<List<UserVideosDTO>> listVideosByUser(@PathVariable("id") Long userId);

}
